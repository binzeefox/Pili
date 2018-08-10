package com.forradical.binzee.collectionforlisab.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.activities.main.MainActivity;
import com.forradical.binzee.collectionforlisab.base.FoxApplication;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具类
 * <p>
 * 照片存放在/image内，以当前毫秒命名
 * 剪切图存放在/crop
 * 缓存存放在/temp
 */
public class FileUtil {
    //文件选取类别
    public static final String FILE_TYPE_IMAGE = "image/*"; //图片
    public static final String FILE_TYPE_AUDIO = "audio/*"; //音频
    public static final String FILE_TYPE_VIDEO = "video/*"; //视频
    public static final String FILE_TYPE_IMAGE_N_VIDEO = "video/*;image/";  //音频和视频
    public static final String FILE_TYPE_ALL = "*/*";   //全部类别

    private static final int REQUEST_FILE = R.id.request_file;  //请求文件请求码

    private static final String EXTERNAL_DIR
            = Environment.getExternalStorageDirectory().getAbsolutePath() + "PIMI";
    private static final String EXTERNAL_OUTPUT_DB_PATH = EXTERNAL_DIR + "/databases";
    private static final String EXTERNAL_IMAGE_STORAGE = EXTERNAL_DIR + "/image";
    private static final String EXTERNAL_APP_IMAGE_DIR = FoxApplication.getInstance()
            .getApplicationContext().getExternalFilesDir("image").getAbsolutePath();

    private static final String DATABASE_DIR = "/databases";
    private static final String DATABASE_FILE_PATH = DATABASE_DIR + "ImagesGallery.db";

    //存储文件AUTH
    private static final String ENTERNAL_STORAGE_AUTHORITY
            = "com.android.externalstorage.documents";
    //下载文件AUTH
    private static final String DOWNLOAD_AUTHORITY
            = "com.android.providers.downloads.documents";
    //媒体文件AUTH
    private static final String MEDIA_AUTHORITY
            = "com.android.providers.media.documents";
    //？？文件AUTH
    private static final String GOOGLE_PHOTOS_AUTHORITY
            = "com.google.android.apps.photos.content";

    private static final String DOWNLOAD_URI
            = "content://downloads/public_downloads";

    //    ****公共方法 ↓

    /**
     * 删除数据
     */
    public static boolean deleteBeanFile(ImageBean bean) {
        File file = new File(bean.getPath());
        return !file.exists() || file.delete();
    }


    /**
     * 将照片考入指定文件（若有同名文件则删除）
     *
     * @param raw 原始文件
     * @return 是否成功
     */
    public static String copyImageToEnternal(File raw) {
        String path = EXTERNAL_IMAGE_STORAGE + "/" + raw.getName();
        File out = new File(path);
        if (out.exists())
            out.delete();
        try {
            copyFileUsingFileStreams(raw, out);
        } catch (IOException e) {
            return null;
        }
        return path;
    }

    /**
     * 将照片考入应用文件夹（若有同名文件则删除）
     *
     * @param raw 原始文件
     * @return 路径
     */
    public static String copyImage(File raw) {
        String path = EXTERNAL_APP_IMAGE_DIR + "/" + raw.getName();
        File out = new File(path);
        if (out.exists())
            out.delete();
        try {
            out.createNewFile();
            copyFileUsingFileStreams(raw, out);
        } catch (IOException e) {
            return null;
        }
        return path;
    }

//    /**
//     * 引入外源数据库
//     *
//     * @param ctx  上下文
//     * @param file 外源数据库
//     * @return 是否成功
//     */
//    public static boolean importDBfile(Context ctx, File file) {
//        if (!file.exists()) {
//            return false;
//        }
//        String path = ctx.getExternalFilesDir(null).getAbsolutePath() + DATABASE_FILE_PATH;
//        File db = new File(path);
//        if (db.exists())
//            db.delete();
//        try {
//            db.createNewFile();
//            copyFileUsingFileStreams(file, db);
//        } catch (IOException e) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 导出外源数据库,导出到
//     *
//     * @param ctx 上下文
//     * @return 是否成功
//     */
//    public static boolean outputDBfile(Context ctx) {
//        String rawPath = ctx.getExternalFilesDir(null).getAbsolutePath() + DATABASE_FILE_PATH;
//        File raw = new File(rawPath);
//        if (!raw.exists()) {
//            return false;
//        }
//        File output = new File(EXTERNAL_OUTPUT_DB_PATH
//                + "ImagesGallery" + System.currentTimeMillis() + ".db");
//        try {
//            if (output.exists()) {
//                output.delete();
//            }
//            output.createNewFile();
//            copyFileUsingFileStreams(raw, output);
//        } catch (IOException e) {
//            return false;
//        }
//        return true;
//    }

    /**
     * 获取图片临时存储路径
     *
     * @param context 上下文
     * @return 路径
     */
    public static String getImageTempPath(Context context) {
        String path = getTempDir(context, "/image");
        try {
            path += "/" + System.currentTimeMillis() + ".jpg";
            File temp = new File(path);
            if (temp.exists())
                temp.delete();
            temp.createNewFile();
        } catch (IOException e) {
            path = null;
        }
        return path;
    }

    /**
     * 获取剪切图缓存路径
     *
     * @param context 上下文
     * @return 路径
     */
    public static String getCropTempPath(Context context) {
        String path = getTempDir(context, "/crop");
        try {
            path += "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(path);
            if (file.exists())
                file.delete();
            file.createNewFile();
        } catch (IOException e) {
            path = null;
        }
        return path;
    }

    /**
     * 将本地文件路径转化成FileProvider Uri
     *
     * @param context 上下文
     * @param path    原始路径
     * @return Uri
     */
    public static Uri getContentUri(Context context, String path) {
        File imageFile = new File(path);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=?"
                , new String[]{path}
                , null);

        if (cursor != null && cursor.moveToFirst()) {
            //如果已经存在于Provider，则利用id直接生成Uri
            int id = cursor
                    .getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                //如果文件存在于本地，则将其存入Provider
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else //图片不存在
                return null;
        }
    }

    /**
     * 通过相册Uri获取图片文件
     *
     * @param rawUri 相册返回的uri
     * @return 目标文件
     */
    public static File getImageFileFromUri(Context context, Uri rawUri) {
        if (DocumentsContract.isDocumentUri(context, rawUri)) {
            //若为Document类型，则通过document id处理
            String docId = DocumentsContract.getDocumentId(rawUri);
            if (MEDIA_AUTHORITY.equals(rawUri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                return getFileFromSelection
                        (context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if (DOWNLOAD_AUTHORITY.equals(rawUri.getAuthority())) {
                Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse(DOWNLOAD_URI), Long.valueOf(docId));
                return getFileFromSelection(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(rawUri.getScheme())) {
            //content类型URI，按照普通方式处理
            return getFileFromSelection(context, rawUri, null);
        } else if (("file".equalsIgnoreCase(rawUri.getScheme()))) {
            //文件类型uri，直接获取
            return new File(rawUri.getPath());
        }
        return null;
    }

    //    ****私有方法 ↓
    private static String getTempDir(Context context, String dirName) {
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
            path = context.getExternalCacheDir().getAbsolutePath();
        else
            path = context.getCacheDir().getAbsolutePath();

        if (path.isEmpty())
            return null;
        path += dirName;
        File dir = new File(path);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdir();
            }
        } else
            dir.mkdir();
        return dir.getAbsolutePath();
    }

    /**
     * 通过Uri和Selection获取真实分检
     *
     * @param uri       uri
     * @param selection selection
     * @return 目标文件
     */
    private static File getFileFromSelection(Context ctx, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = ctx.getContentResolver()
                .query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return new File(path);
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件夹
     * @throws IOException
     */
    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            input.close();
            output.close();
        }
    }
}
