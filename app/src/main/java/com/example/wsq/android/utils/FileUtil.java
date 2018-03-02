package com.example.wsq.android.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.wsq.android.constant.Constant;
import com.orhanobut.logger.Logger;

import org.apache.http.util.ByteArrayBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**

 */
public final class FileUtil {

    public static String DIR = "/xiyi/";
    public static String TD_PRJECT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + DIR;//

    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTdPath(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + DIR;
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + DIR;
        }

        return filePath;
    }

    /**
     * 创建文件夹
     *
     * @param context
     */
    public static File mkdir(Context context) {
        File file;
        file = new File(TD_PRJECT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static void delete(File file) {
        // File file = new File(sd_card + path);
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 保存图片到SD卡
     *
     * @param URL
     * @param data
     * @throws IOException
     */
    public static void saveImage(String URL, byte[] data) throws IOException {
        String name = MyHash.mixHashStr(URL);
        saveData(TD_PRJECT_PATH, name, data);
    }

    /**
     * 读取图片
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] readImage(String filename) throws IOException {
        String name = MyHash.mixHashStr(filename);
        byte[] tmp = readData(TD_PRJECT_PATH, name);
        return tmp;
    }

    /**
     * 读取图片工具
     *
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    private static byte[] readData(String path, String name) throws IOException {
        // String name = MyHash.mixHashStr(url);
        ByteArrayBuffer buffer = null;
        String paths = path + name;
        File file = new File(paths);
        if (!file.exists()) {
            return null;
        }
        InputStream inputstream = new FileInputStream(file);
        buffer = new ByteArrayBuffer(1024);
        byte[] tmp = new byte[1024];
        int len;
        while (((len = inputstream.read(tmp)) != -1)) {
            buffer.append(tmp, 0, len);
        }
        inputstream.close();
        return buffer.toByteArray();
    }

    /**
     * 图片保存工具类
     *
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    private static void saveData(String path, String fileName, byte[] data)
            throws IOException {
        // String name = MyHash.mixHashStr(AdName);
        File file = new File(path + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    /**
     * 判断文件是否存在 true存在 false不存在
     *
     * @param url
     * @return
     */
    public static boolean compare(String url) {
        String name = MyHash.mixHashStr(url);
        String paths = TD_PRJECT_PATH + name;
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }


    public static void savaFileToSD(Context context, String filename, String filecontent) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
           String  fileName = getTdPath(context)+  filename;

            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(fileName);
            output.write(filecontent.getBytes());
            //将String字符串以字节流的形式写入到输出流中
            output.close();
            //关闭输出流
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }


    //定义读取文件的方法:
    public static String readFromSD(Context context, String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileName = getTdPath(context)  + filename;
            //打开文件输入流
            FileInputStream input = new FileInputStream(fileName);
            byte[] temp = new byte[1024];

            int len = 0;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
        }
        return sb.toString();
    }

    /**
     * 复制assets文件到sd卡
     * @param context
     * @param assetsPath
     * @param savePath
     */
    public static void copyFilesFromAssets(Context context, String assetsPath, String savePath){
        Logger.d("复制文件");
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 写一个文件到SDCard
    public static void writeFileToSDCard(String content) throws IOException {
        // 比如可以将一个文件作为普通的文档存储，那么先获取系统默认的文档存放根目录
        File parent_path = Environment.getExternalStorageDirectory();

        // 可以建立一个子目录专门存放自己专属文件
        File dir = new File(Constant.BITMAP_PATH);
        dir.mkdir();

        File file = new File(dir.getAbsoluteFile(), "myfile.txt");

        // 创建这个文件，如果不存在
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = content.getBytes();

        // 开始写入数据到这个文件。
        fos.write(buffer, 0, buffer.length);
        fos.flush();
        fos.close();

    }
}
