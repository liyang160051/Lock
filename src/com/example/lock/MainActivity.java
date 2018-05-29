package com.example.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	private DevicePolicyManager policyManager;
	private ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		// 获取设备管理服务
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

		// AdminReceiver 继承自 DeviceAdminReceiver
		componentName = new ComponentName(this, AdminReceiver.class);

		mylock();
		// killMyself ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
		MainActivity.this.finish();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void mylock() {
		// TODO Auto-generated method stub
		boolean active = policyManager.isAdminActive(componentName);
		if (!active) {
			activeManage();
			active = policyManager.isAdminActive(componentName);
		}
		if (active) {
			policyManager.lockNow();
		}

	}

	private void activeManage() {
		// TODO Auto-generated method stub
		// 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

		// 权限列表
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

		// 描述(additional explanation)
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");

		startActivityForResult(intent, 0);

	}

}
