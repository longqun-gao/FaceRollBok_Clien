package com.example.administrator.face;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.face.bean.Student;
import com.example.administrator.face.tool.CircleImageView;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.ImgTool;
import com.example.administrator.face.tool.ServerUtils;
import com.example.administrator.face.tool.T;
import com.example.administrator.face.util.FaceUtil;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class StuReg extends AppCompatActivity implements View.OnClickListener {

    private ImageView mSturegBack;
    /**
     * 更改资料
     */
    private Button mUpdateBtn;
    private CircleImageView mCv;
    private RelativeLayout mRyCvIv;
    private EditText mStudentZhuceName;
    private EditText mStudentZhucePwd;
    private EditText mStudentZhucePhone;
    private EditText mStudentZhuceClass;
    private EditText mStudentZhuceSex;
    /**
     * 提交注册
     */
    private Button mStudentZhuceSubmit;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String imgurl = null;//图片的路径
    private File tempFile;
    private File file;
    private ImageView stureg_back;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private boolean persionFlag = false;
    private String picpath;
    /***
     * 人脸识别相关
     * */
    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid ="face";
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    //返回
    private ImageView dianming_back;
    private String path;
    private String yzma;//用于接收每次的随机数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_reg);
        initView();
    }

    private void initView() {
        /**
         * 人脸识别相关
         * */
        // 在程序入口处传入appid，初始化SDK
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });
        mFaceRequest = new FaceRequest(this);
        mSturegBack = (ImageView) findViewById(R.id.stureg_back);
        mSturegBack.setOnClickListener(this);
        mUpdateBtn = (Button) findViewById(R.id.update_btn);
        mCv = (CircleImageView) findViewById(R.id.cv);
        mRyCvIv = (RelativeLayout) findViewById(R.id.ry_cv_iv);
        mRyCvIv.setOnClickListener(this);
        mStudentZhuceName = (EditText) findViewById(R.id.student_zhuce_name);
        mStudentZhucePwd = (EditText) findViewById(R.id.student_zhuce_pwd);
        mStudentZhucePhone = (EditText) findViewById(R.id.student_zhuce_phone);
        mStudentZhuceClass = (EditText) findViewById(R.id.student_zhuce_class);
        mStudentZhuceSex = (EditText) findViewById(R.id.student_zhuce_sex);
        mStudentZhuceSubmit = (Button) findViewById(R.id.student_zhuce_submit);
        mStudentZhuceSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stureg_back:
                finish();
                break;
            case R.id.ry_cv_iv:
                ///头像相关
                AlertDialog.Builder builder = new AlertDialog.Builder(StuReg.this);
                builder.setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                gallery();
                                break;
                            case 1:
                                camera();
                                break;
                        }
                    }
                });
                builder.show();
                break;
            case R.id.student_zhuce_submit:
                if(TextUtils.isEmpty(mStudentZhuceName.getText().toString())){
                    T.show("用户名不能为空");
                    return;
                }else if(TextUtils.isEmpty(mStudentZhucePwd.getText().toString())){
                    T.show("密码不能为空");
                    return;
                }  else if(TextUtils.isEmpty(mStudentZhuceClass.getText().toString())){
                    T.show("班级不能为空");
                    return;
                }  else if(TextUtils.isEmpty(mStudentZhuceSex.getText().toString())){
                    T.show("性别不能为空");
                    return;
                }  else if(TextUtils.isEmpty(mStudentZhucePhone.getText().toString())){
                    T.show("电话不能为空");
                    return;
                } else if (TextUtils.isEmpty(path)) {
                    T.show("请选择图片后再注册");
                    return;
                } else {
                   /* mProDialog.setMessage("注册中...");
                    mProDialog.show();*/
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。

                    String s = getNum();
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID,mAuthid+s);
                    Log.e("mAuthid+s",mAuthid+s+"");
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                    Student student = new Student();
                    student.setSname(mStudentZhuceName.getText().toString());
                    student.setSpassword(mStudentZhucePwd.getText().toString());
                    student.setSclass(mStudentZhuceClass.getText().toString());
                    student.setSphone(mStudentZhucePhone.getText().toString());
                    student.setSsex(mStudentZhuceSex.getText().toString());
                    student.setSpicid(mAuthid+s);
                    Gson gson = new Gson();
                    String stu = gson.toJson(student);
                    String flag = "2";

                    zhuCePanDuan(Constant.host+"StuServlet?flag="+flag+
                            "&username="+mStudentZhuceName.getText().toString()+
                            "&password="+mStudentZhucePwd.getText().toString()+
                            "&sclass="+mStudentZhuceClass.getText().toString()+
                            "&phone="+mStudentZhucePhone.getText().toString()+
                            "&sex="+mStudentZhuceSex.getText().toString()+
                            "&spicid="+student.getSpicid().toString()
                    );
                }
                break;
        }
    }
    /**
     * 人脸识别相关
     * */
    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            T.show("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            T.show("头像上传成功");
        } else {
            T.show("注册失败");
        }
    }
    //判断该用户是否已被注册
    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.e("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }
            // T.show(error.getPlainDescription(true));
            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        T.show("id已经被注册，请更换后再试");
                        break;

                    default:
                        T.show(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };
    // 生成验证码
    private String getNum() {
        yzma = "";
        for (int i = 0; i < 4; i++) {
            int a = (int) (Math.random() * 100) + 1;
            if (a % 2 == 0) {
                int p = (int) (Math.random() * 10);
                yzma += p;
            } else {
                char b = (char) ((int) (Math.random() * 26 + 97));
                yzma += b;
            }
        }
        return yzma;
    }
    private void zhuCePanDuan(final String urlpath) {
        new Thread(){
            @Override
            public void run() {
                Log.e("urlpath",urlpath+"");
                Log.e("path",path+"");

                String str = ServerUtils.formUpload(urlpath,path);
                if("注册失败".equals(str)){
                    handler.sendEmptyMessage(0x123);
                }else{
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){
                // Toast.makeText(ZhuCeActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                T.show("注册失败");
            }else if (msg.what == 0x124){
                T.show("信息注册成功");
                finish();
            }
        }
    };


    /*
	 * 从相册获取
	 */
    private void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    private void camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            path=tempFile.getAbsolutePath();
            Log.e("lujing",path+"路径");
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }
    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fileSrc = null;
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imgurl = getImagePath(uri, null);
                String []proj ={MediaStore.Images.Media.DATA};
                //好像是Android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                path = cursor.getString(column_index);
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                T.show("未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(StuReg.this, bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(StuReg.this);
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
                T.show("图片信息无法正常获取！");
                return;
            }
            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();

            ((ImageView) findViewById(R.id.cv)).setImageBitmap(mImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        else{
            path = ImgTool.getImageAbsolutePath(this, uri);
        }
        return path;

    }
}
