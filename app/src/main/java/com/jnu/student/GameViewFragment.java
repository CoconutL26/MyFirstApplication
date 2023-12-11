package com.jnu.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GameViewFragment extends Fragment {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int bookCount = 0;
    private CountDownTimer timer;
    private Book book;
    private TextView timeTextView;
    private TextView countTextView;
    private Bitmap bookImage;
    private ArrayList<Book> books = new ArrayList<>();;



    public GameViewFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public static GameViewFragment newInstance() {
        GameViewFragment fragment = new GameViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_view, container, false);

        surfaceView = view.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        timeTextView = new TextView(requireContext());
        timeTextView.setTextColor(getResources().getColor(android.R.color.black));
        timeTextView.setTextSize(20);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        timeTextView.setLayoutParams(params);

        countTextView = new TextView(requireContext());
        countTextView.setTextColor(getResources().getColor(android.R.color.black));
        countTextView.setTextSize(20);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.TOP | Gravity.LEFT;
        countTextView.setLayoutParams(params2);
        ((FrameLayout) view).addView(timeTextView);
        ((FrameLayout) view).addView(countTextView);

        // 加载书本图片
        bookImage = BitmapFactory.decodeResource(getResources(), R.drawable.book1);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 初始化游戏
                initializeGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // 处理 Surface 改变的情况
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 处理 Surface 销毁的情况
                stopGame();
            }
        });

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 处理触摸事件
                handleTouch(event.getX(), event.getY());
                return true;
            }
        });

        return view;
    }
    private void initializeGame() {
        resetGame();
        // 初始化游戏逻辑
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新剩余时间
                // 在这里可以更新 UI 上的时间显示
                final String timeString = String.format(Locale.getDefault(), "剩余时间: %d 秒", millisUntilFinished / 1000);
                final String CoutString = (String)("已经学了 "+bookCount+" 本了");
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeTextView.setText(timeString);
                        countTextView.setText(CoutString);
                    }
                });
            }

            @Override
            public void onFinish() {
                // 游戏结束
                // 在这里可以显示得分等信息
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showScoreDialog();
                    }
                });
            }
        }.start();

        // 在这里可以启动线程，处理书本的生成和移动逻辑
        // 为了简化，你可以在这里使用 post 方法或者 Handler 来处理书本的生成和移动逻辑
        // 生成多个 Book 对象
        for (int i = 0; i < 5; i++) {
            int displayTime = new Random().nextInt(4000) + 2000;
            Book newBook = generateBook(displayTime);
            books.add(newBook);
        }
        startGameLoop();
    }
    private void showScoreDialog() {
        // 创建AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("游戏结束");
        String scoreMessage = String.format(Locale.getDefault(), "你的得分是：%d", bookCount);
        builder.setMessage(scoreMessage);


        // 创建AlertDialog但不显示
        AlertDialog alertDialog = builder.create();

        // 设置对话框显示在正中间
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        // 设置对话框dismiss监听器，当对话框消失时重新初始化游戏
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                initializeGame();
            }
        });

        // 显示AlertDialog
        alertDialog.show();
    }


    private void resetGame() {
        // 重置游戏状态，清零得分等
        bookCount = 0;
        if (timer != null) {
            timer.cancel();
        }
        // 在这里可以添加其他需要重置的逻辑
        if (books != null) {
            books.clear();
        }
    }
    private void handleTouch(float x, float y) {
        // 处理触摸事件，判断是否碰到书本
        // 如果碰到书本，则书本消失，学到的书本数+1
        for (int i = 0; i < books.size(); i++) {
            Book currentBook = books.get(i);
            if (currentBook != null && currentBook.isTouched(x, y)) {
                bookCount++;
                books.remove(i);
                i--; // 因为删除了一个元素，需要减少索引
            }
        }
    }

    private void stopGame() {
        // 停止游戏，释放资源
        if (timer != null) {
            timer.cancel();
        }
        if (books != null) {
            books.clear();
        }

    }
    private void startGameLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (!Thread.interrupted()) {
                    // 生成书本
                    if (books.size() < 5) {
                        int displayTime = random.nextInt(4000) + 2000; // 2秒到5秒之间的随机时间
                        Book newBook = generateBook(displayTime);
                        books.add(newBook);
                    }

                    // 移动书本
                    for (Book currentBook : books) {
                        if (currentBook != null) {
                            currentBook.move(surfaceView.getHeight(), surfaceView.getWidth());
                        }
                    }


                    // 绘制画面
                    draw();
                }
            }
        }).start();
    }
    private Book generateBook(int displayTime) {
        Random random = new Random();
        int x = random.nextInt(surfaceView.getWidth() - bookImage.getWidth());
        int y = random.nextInt(surfaceView.getHeight() - bookImage.getHeight());
        return new Book(x, y, displayTime);
    }

    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE); // 清空画布

            // 绘制书本
            if (books != null) {
                for(Book book :books) {
                    book.draw(canvas);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private class Book {
        private float x;
        private float y;
        private int displayTime;
        private Paint paint;
        float direction;
        private int speedX = 2; // 可以根据需要调整速度

        public Book(int x, int y, int displayTime) {
            this.x = x;
            this.y = y;
            this.displayTime = displayTime;
            this.paint = new Paint();
        }

        public void draw(Canvas canvas) {
            // 绘制书本图片
            canvas.drawBitmap(bookImage, x, y, paint);
        }

        public boolean isTouched(float touchX, float touchY) {
            // 判断触摸位置是否在书本范围内
            return touchX >= x && touchX <= x + bookImage.getWidth() && touchY >= y && touchY <= y + bookImage.getHeight();
        }
        public void move(float maxHeight, float maxWidth)
        {
            if(Math.random()<0.05)
            {
                setDirection((float) (Math.random()*2*Math.PI));
            }
            x= (float) (x+30*Math.cos(direction));
            y= (float) (y+30*Math.sin(direction));
            if(x<0)x+=maxWidth;
            if(x>maxWidth)x-=maxWidth;
            if(y<0)y+=maxHeight;
            if(y>maxHeight)y-=maxHeight;

        }
        public void setDirection(float direction) {
            this.direction = direction;
        }
    }
}
