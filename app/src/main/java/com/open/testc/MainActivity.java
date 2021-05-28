package com.open.testc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.open.testc.onclickInject.ClickEvent;
import com.open.testc.thread.ThreadLocalDemo;
import com.open.testc.util.InjectOnClickUtil;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private TextView sampleText;
    private Button btJump;

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Nullable
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        InjectOnClickUtil.inject(this);
        InjectOnClickUtil.InjectView(this);

        sampleText = (TextView) findViewById(R.id.sample_text);
        btJump = (Button) findViewById(R.id.bt_jump);

        // Example of a call to a native method
        sampleText.setText(stringFromJNI());
        btJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JumpActivity.class);
                intent.putExtra("name", "testInject");
                intent.putExtra("age", 18);
                startActivity(intent);
            }
        });


        MyThread myThread = new MyThread();
        myThread.start();


        UseRunnable useRunnable = new UseRunnable();
        new Thread(useRunnable).start();

        ThreadLocalDemo.MyThread1 myThread1 = new ThreadLocalDemo.MyThread1();
        myThread1.start();

        ThreadLocalDemo.MyThread2 myThread2 = new ThreadLocalDemo.MyThread2();
        myThread2.start();

        ThreadLocalDemo.printMainThread();

    }

    /**
     * 继承Thread
     */
    private static class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            //do my work
            System.out.println("I am extends Thread");
        }
    }

    /**
     * 实现Runnable
     */
    private static class UseRunnable implements Runnable {

        @Override
        public void run() {
            //do my work
            System.out.println("I am implements Runnable");
        }
    }


    @ClickEvent({R.id.bt_jump_annotation, R.id.bt_jump_annotation2})
    public void testOnclick(View view) {
        //调用的时候需要Button view  有这个参数

        sampleText.setText("点击了测试注解");
        Toast.makeText(this, "测试动态代理点击事件", Toast.LENGTH_SHORT).show();
    }


//    @OnClick({R.id.bt_jump_annotation})
//    public void testOnclick() {
//        sampleText.setText("点击了测试注解");
//    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}