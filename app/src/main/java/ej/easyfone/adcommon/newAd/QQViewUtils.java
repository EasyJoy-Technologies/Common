package ej.easyfone.adcommon.newAd;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class QQViewUtils {

  public static void removeFromParent(View view) {
    if (view != null) {
      ViewParent vp = view.getParent();
      if (vp instanceof ViewGroup) {
        ((ViewGroup) vp).removeView(view);
      }
    }
  }
}
