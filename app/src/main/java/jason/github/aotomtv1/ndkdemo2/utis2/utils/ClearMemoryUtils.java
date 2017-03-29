package jason.github.aotomtv1.ndkdemo2.utis2.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 内存清理
 * 
 * @author 邹观荣
 * 
 */
public class ClearMemoryUtils {

	private String TAG = "ClearMemoryUtils";

	/**
	 * 获取android当前可用内存大小
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static long getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
		return mi.availMem;
	}

	/**
	 * 获取总内存
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		// Formatter.formatFileSize(context, initial_memory);//
		// Byte转换为KB或者MB，内存大小规格化
		return initial_memory;
	}

	/**
	 * 获取已使用的内存 %
	 * 
	 * @return
	 */
	public static String getUseMemory(long total, long can) {
		String result = "0%";
		long unuse = total - can;
		BigDecimal b1 = new BigDecimal(Double.valueOf(total));
		BigDecimal b2 = new BigDecimal(Double.valueOf(unuse));
		result = b2.divide(b1, 2, BigDecimal.ROUND_HALF_UP) + "%";
		return result;
	}

	/** * 计算已使用内存的百分比 * */
	public static String getUsedPercentValue(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine
					.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
					"\\D+", ""));
			long availableSize = getAvailMemory(context) / 1024;
			int percent = (int) ((totalMemorySize - availableSize)
					/ (float) totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "无结果";
	}

	/**
	 * 清理内存
	 * 
	 * @param context
	 */
	public static String  memoryCleanup(Context context) {
		String result="";
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
//		List<ActivityManager.RunningServiceInfo> serviceInfos = am
//				.getRunningServices(100);
		long beforeMem = getAvailMemoryFormat(context);
		int count = 0;
		if (infoList != null) {
			for (int i = 0; i < infoList.size(); ++i) {
				RunningAppProcessInfo appProcessInfo = infoList.get(i);
				// Log.d(TAG, "process name : " + appProcessInfo.processName);
				// importance 该进程的重要程度 分为几个级别，数值越低就越重要。
				// Log.d(TAG, "importance : " + appProcessInfo.importance);
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
				if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					String[] pkgList = appProcessInfo.pkgList;
					for (int j = 0; j < pkgList.length; ++j) {
						// pkgList 得到该进程下运行的包名
						//Log.i("Clear", "It will be killed, package name : "+ pkgList[j]);
						am.killBackgroundProcesses(pkgList[j]);
						count++;
					}
				}

			}
		}
		long afterMem = getAvailMemoryFormat(context);
//		Toast.makeText(context,
//				"clear " + count + " process, " + (afterMem - beforeMem) + "M",
//				Toast.LENGTH_LONG).show();
		result="clear " + count + " process, " + (afterMem - beforeMem) + "M";
		return result;
	}

	// 获取可用内存大小
	@SuppressWarnings("unused")
	public static long getAvailMemoryFormat(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
		// Log.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024));
		return mi.availMem / (1024 * 1024);
	}

}
