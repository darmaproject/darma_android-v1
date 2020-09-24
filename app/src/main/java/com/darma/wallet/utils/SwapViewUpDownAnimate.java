package com.darma.wallet.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Darma Project on 2019/12/6.
 */
public class SwapViewUpDownAnimate {


    private static final long DURATION = 1000;
    boolean onAnimate=false;

     View upView;
     View downView;

    public View getUpView() {
        return upView;
    }

    public void setUpView(View upView) {
        this.upView = upView;
    }

    public View getDownView() {
        return downView;
    }

    public void setDownView(View downView) {
        this.downView = downView;
    }

    public boolean isOnAnimate(){
        return onAnimate;
    }
    public  void swapViewUpDown() {

//        if(onAnimate){
//            return;
//        }


        onAnimate=true;
        downView.animate().translationYBy(upView.getTop() - downView.getTop())
                .setDuration(DURATION).start();
        upView.animate().translationYBy(downView.getTop()-upView.getTop())
                .setDuration(DURATION).start();
        downView.animate()
                .scaleX(1.1f).scaleY(1.1f).setDuration(DURATION/2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        downView.animate().scaleY(1f).scaleX(1f).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
//                                if(onAnimate){
//                                    onAnimate=false;
//                                }else{
//                                    listener.onAnimationFinish();
//                                }
                                
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).setDuration(DURATION/2).start();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        upView.animate().scaleX(0.8f).scaleY(0.8f)
                .alpha(0.5f)
                .setDuration(DURATION/2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        upView.animate().scaleY(1f).scaleX(1f).alpha(1).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
//                                if(onAnimate){
//                                    onAnimate=false;
//                                }else{
//                                    listener.onAnimationFinish();
//                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).setDuration(DURATION/2).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
        startMonitor();
    }

    private void startMonitor() {

        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(DURATION+500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onAnimate=false;
            }
        });
    }


    public  void swapViewBack() {
//        if(onAnimate){
//            return;
//        }

        onAnimate=true;

        downView.animate().translationYBy( downView.getTop()-upView.getTop())
                .setDuration(DURATION).start();
        upView.animate().translationYBy(upView.getTop()-downView.getTop())
                .setDuration(DURATION).start();
        downView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(DURATION/2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        downView.animate().scaleY(1f).scaleX(1f)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
//                                        if(onAnimate){
//                                            onAnimate=false;
//                                        }else{
//                                            listener.onAnimationFinish();
//                                        }

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).setDuration(DURATION/2).start();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        upView.animate().scaleX(0.8f).scaleY(0.8f)
                .alpha(0.5f)
                .setDuration(DURATION/2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        upView.animate().scaleY(1f).scaleX(1f).alpha(1).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
//                                if(onAnimate){
//                                    onAnimate=false;
//                                }else{
//                                    listener.onAnimationFinish();
//                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).setDuration(DURATION/2).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        startMonitor();
    }


}
