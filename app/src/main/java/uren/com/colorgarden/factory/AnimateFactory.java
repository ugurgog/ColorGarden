package uren.com.colorgarden.factory;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import uren.com.colorgarden.R;

public class AnimateFactory {
    private static AnimateFactory animateFactory;

    private AnimateFactory() {
    }

    public static AnimateFactory getInstance() {
        if (animateFactory == null) {
            animateFactory = new AnimateFactory();
        }
        return animateFactory;
    }

    public void SlideOutUpAnimation(View... view) {
        for (final View v : view) {
            if (v.getVisibility() == View.VISIBLE) {
                Animator.AnimatorListener listener = new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                };
                YoYo.with(Techniques.FadeOut).withListener(listener).duration(300).playOn(v);
            }
        }
    }

    public void BounceInDownAnimation(View... view) {
        for (View v : view) {
            if (v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(300).playOn(v);
            }
        }
    }

    public Animation popupAnimation(Context context) {
        Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_show_overshoot);
        return scaleAnimation;
    }
}
