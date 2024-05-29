package krcsn.ppanel;

import java.util.Arrays;

public class Easing {
  private Easing () {
  }
	
 /** Enumerator used for easeFunction(), and interpolate() */
  public static enum Blend {
    constant,
      linear,
      easeIn,
      easeOut,
      easeInOut,
      easeInSine,
      easeOutSine,
      easeInOutSine,
      easeInQuad,
      easeOutQuad,
      easeInOutQuad,
      easeInCubic,
      easeOutCubic,
      easeInOutCubic,
      easeInQuart,
      easeOutQuart,
      easeInOutQuart,
      easeInQuint,
      easeOutQuint,
      easeInOutQuint,
      easeInExpo,
      easeOutExpo,
      easeInOutExpo,
      easeInCirc,
      easeOutCirc,
      easeInOutCirc,
      easeInBack,
      easeOutBack,
      easeInOutBack,
      easeInElastic,
      easeOutElastic,
      easeInOutElastic,
      easeInBounce,
      easeOutBounce,
      easeInOutBounce
  }
  
  /** @return Easing.Blend from integer*/
  public static Blend getBlend(int type) {
    return Blend.values()[type];
  }
	
  /** @return easing of (x), with ease type of blend*/
  public static float easeFunction(float x, Blend blend) {
    switch (blend) {
    case constant:
      return (int)x;
    case linear:
      return x;
    case easeIn:
      return easeIn(x);
    case easeOut:
      return easeOut(x);
    case easeInOut:
      return easeInOutQuad(x);
    case easeInSine:
      return easeInSine(x);
    case easeOutSine:
      return easeOutSine(x);
    case easeInOutSine:
      return easeInOutSine(x);
    case easeInQuad:
      return easeInQuad(x);
    case easeOutQuad:
      return easeOutQuad(x);
    case easeInOutQuad:
      return easeInOutQuad(x);
    case easeInCubic:
      return easeInCubic(x);
    case easeOutCubic:
      return easeOutCubic(x);
    case easeInOutCubic:
      return easeInOutCubic(x);
    case easeInQuart:
      return easeInQuart(x);
    case easeOutQuart:
      return easeOutQuart(x);
    case easeInOutQuart:
      return easeInOutQuart(x);
    case easeInQuint:
      return easeInQuint(x);
    case easeOutQuint:
      return easeOutQuint(x);
    case easeInOutQuint:
      return easeInOutQuint(x);
    case easeInExpo:
      return easeInExpo(x);
    case easeOutExpo:
      return easeOutExpo(x);
    case easeInOutExpo:
      return easeInOutExpo(x);
    case easeInCirc:
      return easeInCirc(x);
    case easeOutCirc:
      return easeOutCirc(x);
    case easeInOutCirc:
      return easeInOutCirc(x);
    case easeInBack:
      return easeInBack(x);
    case easeOutBack:
      return easeOutBack(x);
    case easeInOutBack:
      return easeInOutBack(x);
    case easeInElastic:
      return easeInElastic(x);
    case easeOutElastic:
      return easeOutElastic(x);
    case easeInOutElastic:
      return easeInOutElastic(x);
    case easeInBounce:
      return easeInBounce(x);
    case easeOutBounce:
      return easeOutBounce(x);
    case easeInOutBounce:
      return easeInOutBounce(x);
    default:
      return x;
    }
  }

  /** Most popular function easeIn() */
  public static float easeIn(float x) {
    return x * x * x;
  }

  /** Most popular function easeOut() */
  public static float easeOut(float x) {
    return 1 - (float)(Math.pow(1 - x, 4));
  }

  public static float easeInSine(float x) {
    double v = 1 - Math.cos((x * Math.PI) / 2);
    return (float)v;
  }

  public static float easeOutSine(float x) {
    double v = Math.sin((x * Math.PI) / 2);
    return (float)v;
  }

  public static float easeInOutSine(float x) {
    double v = -(Math.cos(Math.PI * x) - 1) / 2;
    return (float)v;
  }

  public static float easeInQuad(float x) {
    return x * x;
  }

  public static float easeOutQuad(float x) {
    return 1 - (1 - x) * (1 - x);
  }

  public static float easeInOutQuad(float x) {
    double v = x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
    return (float)v;
  }

  public static float easeInCubic(float x) {
    return x * x * x;
  }

  public static float easeOutCubic(float x) {
    double v = 1 - Math.pow(1 - x, 3);
    return (float)v;
  }

  public static float easeInOutCubic(float x) {
    double v = x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    ;
    return (float)v;
  }

  public static float easeInQuart(float x) {
    return x * x * x * x;
  }

  public static float easeOutQuart(float x) {
    double v = 1 - Math.pow(1 - x, 4);
    return (float)v;
  }

  public static float easeInOutQuart(float x) {
    double v = x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2;
    return (float)v;
  }

  public static float easeInQuint(float x) {
    return x * x * x * x * x;
  }

  public static float easeOutQuint(float x) {
    double v = 1 - Math.pow(1 - x, 5);
    return (float)v;
  }

  public static float easeInOutQuint(float x) {
    double v = x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
    return (float)v;
  }

  public static float easeInCustom(float x, float g) {
    return (float)Math.pow(x, g);
  }

  public static float easeOutCustom(float x, float g) {
	return (float)(1 - Math.pow(1 - x, g));
  }

  public static float easeInOutCustom(float x, float g) {
  	if (x < 0.5) {
      return (float)(Math.pow(2 * x, g) / 2);
	} else {
	  return (float)(1 - Math.pow(-2 * x + 2, g) / 2);
	}
  }

  public static float easeInExpo(float x) {
    double v = x == 0 ? 0 : Math.pow(2, 10 * x - 10);
    return (float)v;
  }

  public static float easeOutExpo(float x) {
    double v = x == 1 ? 1 : 1 - Math.pow(2, -10 * x);
    return (float)v;
  }

  public static float easeInOutExpo(float x) {
    double v = x == 0
      ? 0
      : x == 1
      ? 1
      : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
      : (2 - Math.pow(2, -20 * x + 10)) / 2;
    return (float)v;
  }

  public static float easeInCirc(float x) {
    double v = 1 - Math.sqrt(1 - Math.pow(x, 2));
    return (float)v;
  }

  public static float easeOutCirc(float x) {
    double v = Math.sqrt(1 - Math.pow(x - 1, 2));
    return (float)v;
  }

  public static float easeInOutCirc(float x) {
    double v = x < 0.5
      ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
      : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2;
    return (float)v;
  }

  public static float easeInBack(float x) {
    final double c1 = 1.70158;
    final double c3 = c1 + 1;
    double v = c3 * x * x * x - c1 * x * x;
    return (float)v;
  }

  public static float easeOutBack(float x) {
    final double c1 = 1.70158;
    final double c3 = c1 + 1;
    double v = 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
    return (float)v;
  }

  public static float easeInOutBack(float x) {
    final double c1 = 1.70158;
    final double c2 = c1 * 1.525;

    double v = x < 0.5
      ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
      : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
    return (float)v;
  }

  public static float easeInElastic(float x) {
    final double c4 = (2 * Math.PI) / 3;

    double v = x == 0
      ? 0
      : x == 1
      ? 1
      : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4);
    return (float)v;
  }

  public static float easeOutElastic(float x) {
    final double c4 = (2 * Math.PI) / 3;

    double v = x == 0
      ? 0
      : x == 1
      ? 1
      : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
    return (float)v;
  }

  public static float easeInOutElastic(float x) {
    final double c5 = (2 * Math.PI) / 4.5;

    double v = x == 0
      ? 0
      : x == 1
      ? 1
      : x < 0.5
      ? -(Math.pow(2, 20 * x - 10) * Math.sin((20 * x - 11.125) * c5)) / 2
      : (Math.pow(2, -20 * x + 10) * Math.sin((20 * x - 11.125) * c5)) / 2 + 1;
    return (float)v;
  }

  public static float easeInBounce(float x) {
    return 1 - easeOutBounce(1 - x);
  }

  public static float easeOutBounce(float x) {
    final double n1 = 7.5625;
    final double d1 = 2.75;

    if (x < 1 / d1) {
      return (float)(n1 * x * x);
    } else if (x < 2 / d1) {
      return (float)(n1 * (x -= 1.5 / d1) * x + 0.75);
    } else if (x < 2.5 / d1) {
      return (float)(n1 * (x -= 2.25 / d1) * x + 0.9375);
    } else {
      return (float)(n1 * (x -= 2.625 / d1) * x + 0.984375);
    }
  }

  public static float easeInOutBounce(float x) {
    return x < 0.5
      ? (1 - easeOutBounce(1 - 2 * x)) / 2
      : (1 + easeOutBounce(2 * x - 1)) / 2;
  }

  public static float cubicBezier(float t, float p0, float p1, float p2, float p3) {
    float u = 1 - t;
    float tt = t * t;
    float uu = u * u;
    float uuu = uu * u;
    float ttt = tt * t;

    float result = (uuu * p0) + (3 * uu * t * p1) + (3 * u * tt * p2) + (ttt * p3);
    return result;
  }

  /** Class what represents Keyframe. Used for interpolate() function */
  public static class Key {
    public float time, value;
    public Blend blend;

    public Key(float time, float value, Blend blend) {
      this.time = time;
      this.value = value;
      this.blend = blend;
    }
  }
  
  public static interface CustomEase {
	public float ease(float x);
  }
  
  public static Key[] sortKeys(Key[] keys) {
    Key[] newArray = Arrays.copyOf(keys, keys.length);

    Arrays.sort(newArray, (k1, k2) -> Float.compare(k1.time, k2.time));

    return newArray;
  }

  /** Blends keyframes (Key[]), and uses t (time), as blend time. Keyframes need to be sorted by time, from minimum to maximum. Use 'Key[] sortKeys(Key[] keys)' function, to sort keyframes time. */
  public static float interpolate(Key[] points, float t) {
    if (points.length < 2) {
      throw new IllegalArgumentException("At least two control points are required.");
    }

    Key start = points[0];
    Key end = points[points.length - 1];

    for (int i = 1; i < points.length; i++) {
      if (t <= points[i].time) {
        start = points[i - 1];
        end = points[i];
        break;
      }
    }

    float localT = (t - start.time) / (end.time - start.time);
    return blendValues(start.value, end.value, localT, start.blend);
  }
  
  /** Simplified creation of keyframe*/
  public static Key key(float time, float value, Blend blend) {
    return new Key(time, value, blend);
  }

  /** Blends values (from a to b) using ease function, defined by Blend */
  public static float blendValues (float a, float b, float x, Blend blend) {
    return lerp(a, b, easeFunction(x, blend));
  }
  
  /** Blends values (from a to b) using ease function, defined by CustomEase */
  public static float blendValues (float a, float b, float x, CustomEase ease) {
    return lerp(a, b, ease.ease(x));
  }

  /** Linear interpolate */
  private static float lerp(float a, float b, float alpha) {
    return a + alpha * (b - a);
  }
}