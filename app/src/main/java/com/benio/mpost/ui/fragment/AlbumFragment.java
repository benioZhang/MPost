package com.benio.mpost.ui.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.AlbumAdapter;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.ImageFile;
import com.benio.mpost.bean.ImageFolder;
import com.benio.mpost.bean.ImageRootFolder;
import com.benio.mpost.util.AKDialog;
import com.benio.mpost.util.AKSystem;
import com.benio.mpost.widget.DividerGridItemDecoration;
import com.benio.mpost.widget.ImageFolderListPopupWindow;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 相册
 * Created by benio on 2015/10/16.
 */
public class AlbumFragment extends RecyclerFragment {
    /**
     * 传参key:最大选择图片的数量
     */
    public static final String BUNDLE_KEY_MAX_COUNT = "BUNDLE_KEY_MAX_COUNT";
    /**
     * 传参key:传递请求码
     */
    public static final String BUNDLE_KEY_GALLERY_REQUEST = "BUNDLE_KEY_GALLERY_REQUEST";
    /**
     * 传参key:选中的图片路径
     */
    public static final String BUNDLE_KEY_SELECTED_IMAGE_PATH = "BUNDLE_KEY_SELECTED_IMAGE_PATH";
    /**
     * 打开相机请求码
     */
    private static final int REQUEST_CAMERA = 0x00018;
    /**
     * popupWindow占屏幕高度百分比
     */
    private static final double POPUP_WINDOW_RATIO = 0.7;
    /**
     * 当前文件夹
     */
    @Bind(R.id.tv_album_folder)
    protected TextView mFolderNameTextView;
    /**
     * 当前文件夹的图片数量
     */
    @Bind(R.id.tv_album_count)
    protected TextView mImageCountTextView;
    /**
     * 图片文件夹弹出框
     */
    private ImageFolderListPopupWindow mPopupWindow;

    private AlbumAdapter mAdapter;

    /**
     * 根目录
     */
    private ImageRootFolder mRootFolder;
    /**
     * 请求码，表示该请求来自哪里
     */
    private int mRequestCode = -1;
    /**
     * 图片保存路径
     */
    private String mPhotoSavePath;
    /**
     * 扫描图片线程
     */
    private Thread mScanImageThread;
    /**
     * 最大选择图片的数量
     * 默认值1
     */
    private int mMaxCount = 1;
    /**
     * 每行显示的图片数量
     */
    private int mSpanCount;
    /**
     * 已选中的图片文件
     */
    private List<ImageFile> mSelectedImageFileList;
    /**
     * 扫描线程回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            hideProgress();
            if (msg.what == ScanImageThread.MSG_SCAN_IMAGE_DONE) {
                mScanImageThread = null;
                //为View绑定数据
                data2View();
                //初始化popupWindow
                initPopupWindow();
            } else {
                showToast(R.string.info_retry);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通知Fragment调用onCreateOptionsMenu()方法
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_commit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_commit);
        int selectedCount = getSelectedImageFileCount();
        String exTitle = item.getTitle().toString();
        item.setEnabled(selectedCount > 0);
        item.setTitle(String.format(exTitle + "(%d/%d)", selectedCount, mMaxCount));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_commit) {
            String[] result = new String[mSelectedImageFileList.size()];
            for (int i = 0; i < mSelectedImageFileList.size(); i++) {
                result[i] = mSelectedImageFileList.get(i).getPath();
            }
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_KEY_SELECTED_IMAGE_PATH, result);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentResource() {
        return R.layout.fragment_album;
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        //这里传Null，等扫描线程结束再设置adapter
        return null;
    }

    @Override
    public RecyclerView.ItemDecoration onCreateItemDecoration() {
        return new DividerGridItemDecoration(getActivity());
    }

    @Override
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new GridLayoutManager(getActivity(), mSpanCount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            mMaxCount = args.getInt(BUNDLE_KEY_MAX_COUNT, mMaxCount);
        }

        //获取每行显示的图片数量
        mSpanCount = getResources().getInteger(R.integer.album_span_count);

        //默认显示所有图片
        mRootFolder = new ImageRootFolder(File.listRoots()[0]);
        mRootFolder.setName(getString(R.string.all_photo));
        mRootFolder.setSelected(true);

        //初始化list
        mSelectedImageFileList = new ArrayList<>();
        //扫描
        scanImage();
    }

    /**
     * 显示PopupWindow
     */
    @OnClick(R.id.rl_album_bottom)
    void onShowPopupWindowEvent(View view) {
        if (null == mPopupWindow) {
            return;
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAsDropDown(view);
        }
    }

    /**
     * 扫描图片
     */
    private void scanImage() {
        if (!AKSystem.isExternalStorageMounted()) {
            showToast(R.string.info_no_storage);
            return;
        }

        showProgress(R.string.scanning);

        mScanImageThread = new ScanImageThread(mRootFolder, mHandler);
        mScanImageThread.start();
    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        //包括根目录的文件夹列表
        List<ImageFolder> datas = new ArrayList<>();
        datas.add(mRootFolder);
        if (mRootFolder.getFolderList() != null) {
            datas.addAll(mRootFolder.getFolderList());
        }

        int height = (int) (AKSystem.getScreenHeight(getActivity()) * POPUP_WINDOW_RATIO);//popupWindow高度
        mPopupWindow = ImageFolderListPopupWindow.newInstance(getActivity(), ViewGroup.LayoutParams.MATCH_PARENT,
                height, datas);
        mPopupWindow.setOnImageFolderClickListener(new ImageFolderListPopupWindow.OnImageFolderClickListener() {
            @Override
            public void onImageFolderClick(ImageFolder imageFolder, int position) {
                if (!mPopupWindow.isFolderSelected(position)) {
                    mAdapter.setData(imageFolder.listFiles());
                    setupFolderNameAndCount(imageFolder);
                }
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 设置文件夹名称和对应的图片数量
     *
     * @param imageFolder
     */
    private void setupFolderNameAndCount(ImageFolder imageFolder) {
        mFolderNameTextView.setText(imageFolder.getName());
        mImageCountTextView.setText(imageFolder.getCount() + "张");
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        List<ImageFile> imageFileList = mRootFolder.listFiles();
        if (imageFileList == null) {
            AKDialog.getMessageDialog(getActivity(), getString(R.string.scan_failed)).show();
            return;
        }

        setupFolderNameAndCount(mRootFolder);

        mAdapter = new AlbumAdapter(getActivity(), imageFileList);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFile imageFile = mAdapter.getItem(position);
                boolean contained = mSelectedImageFileList.contains(imageFile);
                //假设选中该图片后，选中图片数量大于最大数量
                if (!contained && getSelectedImageFileCount() + 1 > mMaxCount) {
                    showToast("你最多只能选择" + mMaxCount + "张照片");
                } else {
                    //如果已经选中则remove，否则add
                    if (!contained) {
                        mSelectedImageFileList.add(imageFile);
                    } else {
                        mSelectedImageFileList.remove(imageFile);
                    }
                    mAdapter.setSelected(position, !mAdapter.isSelected(position));
                    //刷新toolbar上的MenuItem
                    getActivity().invalidateOptionsMenu();
                }
            }
        });
        setAdapter(mAdapter);
    }

    /**
     * @return 选中的ImageFile数量
     */
    private int getSelectedImageFileCount() {
        List<ImageFile> list = mSelectedImageFileList;
        return list != null ? list.size() : 0;
    }

    /**
     * 扫描手机图片线程
     */
    public static class ScanImageThread extends Thread {
        /** 扫描结束后发送的消息 */
        public static final int MSG_SCAN_IMAGE_DONE = 0x001;
        public static final int MSG_SCAN_IMAGE_FAILURE = 0x000;

        /** 临时的辅助类，用于防止同一个文件夹的多次扫描 */
        private HashSet<String> mParentPathSet = new HashSet<>();

        private WeakReference<ImageRootFolder> mFolder;
        private WeakReference<Handler> mHandler;

        public ScanImageThread(ImageRootFolder rootFolder, Handler handler) {
            this.mFolder = new WeakReference<ImageRootFolder>(rootFolder);
            this.mHandler = new WeakReference<Handler>(handler);
        }

        @Override
        public void run() {
            if (mFolder.get() == null) {
                Handler handler = mHandler.get();
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_SCAN_IMAGE_FAILURE);
                }
                return;
            }

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Uri uri = intent.getData();

            ContentResolver contentResolver = AppContext.getInstance().getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(uri, null,
                        null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");//按修改时间排序，倒序

                scan(cursor);
            } catch (Exception e) {
                Handler handler = mHandler.get();
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_SCAN_IMAGE_FAILURE);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            // 扫描完成,释放内存
            Handler handler = mHandler.get();
            if (handler != null) {
                handler.sendEmptyMessage(MSG_SCAN_IMAGE_DONE);
            }
            mFolder = null;
            mHandler = null;
            mParentPathSet.clear();
            mParentPathSet = null;
            System.gc();
        }

        /**
         * 扫描图片
         */
        private void scan(Cursor cursor) {
            if (null == cursor) {
                return;
            }

            ImageRootFolder rootFolder = mFolder.get();

            while (cursor.moveToNext()) {
                // 获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                if (null == rootFolder.getFirstImagePath()) {
                    rootFolder.setFirstImagePath(path);
                }

                File imageFile = new File(path);
                File parentFile = imageFile.getParentFile();

                if (parentFile != null) {
                    String parentPath = parentFile.getAbsolutePath();
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (!mParentPathSet.contains(parentPath)) {
                        ImageFolder imageFolder = createImageFolder(parentFile, path);
                        rootFolder.add(imageFolder);
                        mParentPathSet.add(parentPath);
                    }
                } else {
                    //没有parentFile的图片，说明它在根目录下
                    ImageFolder imageFolder = createImageFolder(imageFile, path);
                    rootFolder.add(imageFolder);
                }
            }
        }


        /**
         * 生成ImageFolder
         */
        private ImageFolder createImageFolder(File file, String firstImagePath) {
            if (null == file) {
                return null;
            }
            ImageFolder imageFolder = new ImageFolder(file);
            imageFolder.setName(file.getName());
            imageFolder.setFirstImagePath(firstImagePath);
            return imageFolder;
        }
    }
}
