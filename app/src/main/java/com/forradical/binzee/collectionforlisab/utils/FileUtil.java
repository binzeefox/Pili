package com.forradical.binzee.collectionforlisab.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * 照片存放在/image内，以当前毫秒命名
 * 剪切图存放在/crop
 * 缓存存放在/temp
 */
public class FileUtil {

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
     * 获取图片临时存储路径
     * @param context   上下文
     * @return  路径
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
     * @param context   上下文
     * @return  路径
     */
    public static String getCropTempPath(Context context){
        String path = getTempDir(context, "/crop");
        try {
            path += "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(path);
            if (file.exists())
                file.delete();
            file.createNewFile();
        }catch (IOException e){
            path = null;
        }
        return path;
    }

    /**
     * 将本地文件路径转化成FileProvider Uri
     * @param context   上下文
     * @param path  原始路径
     * @return  Uri
     */
    public static Uri getContentUri(Context context, String path){
        File imageFile = new File(path);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ,new String[]{MediaStore.Images.Media._ID}
                ,MediaStore.Images.Media.DATA + "=?"
                ,new String[]{path}
                ,null);

        if (cursor != null && cursor.moveToFirst()){
            //如果已经存在于Provider，则利用id直接生成Uri
            int id = cursor
                    .getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()){
                //如果文件存在于本地，则将其存入Provider
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }else //图片不存在
                return null;
        }
    }

    /**
     * 通过相册Uri获取图片文件
     * @param rawUri    相册返回的uri
     * @return  目标文件
     */
    public static File getImageFileFromUri(Context context, Uri rawUri) {
        if (DocumentsContract.isDocumentUri(context, rawUri)){
            //若为Document类型，则通过document id处理
            String docId = DocumentsContract.getDocumentId(rawUri);
            if (MEDIA_AUTHORITY.equals(rawUri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                return getFileFromSelection
                        (context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if (DOWNLOAD_AUTHORITY.equals(rawUri.getAuthority())){
                Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse(DOWNLOAD_URI), Long.valueOf(docId));
                return getFileFromSelection(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(rawUri.getScheme())){
            //content类型URI，按照普通方式处理
            return getFileFromSelection(context, rawUri, null);
        } else if (("file".equalsIgnoreCase(rawUri.getScheme()))){
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
}
