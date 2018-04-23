import java.lang.Math;
/*
doc:https://docs.oracle.com/javase/6/docs/api/java/lang/Math.html#signum(float)
    CODY AND WAIT for range reduction:
    https://books.google.fr/books?id=eNaCDQAAQBAJ&pg=PA203&lpg=PA203&dq=cody+and+waite&source=bl&ots=1Dna2m4Q6G&sig=fQDObB8I2Xq6Jhq_QhIoq-Zdikk&hl=fr&sa=X&ved=0ahUKEwijjebQwtLYAhWEmLQKHcT8CtkQ6AEITjAJ#v=onepage&q=cody%20and%20waite&f=false
*/


public class MathForDeca{
  /* Best representation of PI by a float number */
  public static final float HALFPI = 1.5707963267948966f;
  public static final float PI = 3.1415926535897932f;
  public static final float TWOPI = 6.283185307179586f; // 2*PI
  public static final float FLOAT_MAX = Float.MAX_VALUE;
  public static final float FLOAT_MIN = Float.MIN_VALUE;
  public static final float C1 = 6.28125f; // nombre représentable exactement en machine
  public static final float C2 = 0.001935307179586f; // C = C1 + C2
  public static final float C3 = 1.5703125f; // nombre représentable exactement en machine (1.1001001) proche de PI/2
  public static final float C4 = 0.000483826794896f; // reste tel que C1 + C1 = PI/2

  public static float pow(float x, int n){
    /*Power function*/
    if (n == 0){
      return 1;
    }

    else if (n < 0){
      return 1/pow(x, -n);
    }

    float res = x;
    while (n > 1){
      res *= x;
      n--;
    }
    return res;
  }




  public static float abs(float x){
    /*Abs function */
    return x >= 0 ? x : -x;
  }



  public static float fact(int n){
    /* Factorial function using recursivity */
    if (n < 0){
      System.out.println("Factorial function does not accept negative argument !");
      System.out.println("Error: 0 returned");
      return 0;
    }
    else if (n == 0){
      return 1;
    }
    else{
      float res = 1;
      while (n > 1){
        res *= n;
        n--;
      }
      return res;
    }
  }



  public static float sqrt(float x){
      /* Babylonian Method for Square root
         This is based on the fact the the serie x_{n+1} = 0.5*(x_{n} + S/x_{n})
         converges to sqrt(S) */
    float accuracy = pow(2,-10);

    if (x < 0){
      System.out.println("Square root function does not accept negative argument !");
      System.out.println("Error: 0 returned");
      return 0;
    }
    if (x == 0.0f || x == 1.0f){
      return x;
    }

    /* We use the trick to multiplie values < 1 by 10000 in order to improve accuracy,
    since 10000 is the perfect square of 100 it will be easy to find the answer.*/
    Boolean shifted = false;
    if (x < 1){
      x = x * 10000;
      shifted = true;
    }

    float initialGuess = x/2; //Chosen arbitrary, improve it for faster convergence
    while (abs(initialGuess * initialGuess - x) > accuracy) {
      initialGuess = 0.5f * (initialGuess + x / initialGuess);
    }
    return shifted? initialGuess/100 : initialGuess;
  }


  public static float ulp(float x){
    //[sign] 1.[mantissa] × 2^[exponent]
    //http://www.exploringbinary.com/the-spacing-of-binary-floating-point-numbers/
    /* The ulp is the gap size between the floating point in argument and the next floating point.
       All ulp between 2 floating points located in the same interval [2^e, 2^(e+1)] are the same.
       With e the exponent, we know that x is int [2^e, 2^(e+1)[ and p the precision we know that we have 2^(p-1) gap
       Thus, ulp = 2e/2p-1 = 2e-(p-1) = 2e+1-p*/
    int exponent = 0;
    int precision = 24; //single precision representation

    //ulp is symetric
    x = abs(x);

    if(x == FLOAT_MAX){
      // 104 = 127 (max exponent) + 1 - 24 (precision)
			return pow(2, 104);
    }

    if(x == 0){
    //  System.out.println(Float.MIN_VALUE);
      return FLOAT_MIN;
    }

    //Now we have to compute the exponent ie in which interval [2^e, 2^(e+1)[ we are.
    if(x >= 1){
      //We divide it by 2 until we have smth between [1,2[
			while (x >= 2 ) {
				x =  x/2f;
				exponent ++ ;
      }
    }

    else{
      while(x < 1){
        //We multiply it by 2 until we reach smth in [1,2[
        x = x*2.0f;
        exponent--;
      }
    }
    return pow(2, exponent + 1 - precision);
  }



  // public static float cos(float x){
  //     if(x < 0){
  //       x = -x; // cos(-x) = cos(x)
  //     }
  //     ReductionHandler reduc = new ReductionHandler();
  //     reduc.reduction(x);
  //     x = reduc.getX();
  //     int k = reduc.getK();
  //     k = k % 4;
  //
  //     if(k == 0){
  //       return cosMaclaurin(x);
  //     }else if(k == 1){
  //       return -sinMaclaurin(x);
  //     }else if(k == 2){
  //       return -cosMaclaurin(x);
  //     }else{
  //       return sinMaclaurin(x);
  //     }
  //
  // }
  //
  // public static float sin(float x){
  //     int sign = 1; // sin(-x) = -sin(x)
  //     if(x < 0){
  //       x = -x;
  //       sign = -1;
  //     }
  //     ReductionHandler reduc = new ReductionHandler();
  //     reduc.reduction(x);
  //     x = reduc.getX();
  //     int k = reduc.getK();
  //     k = k % 4;
  //
  //     if(k == 0){
  //       return sign*sinMaclaurin(x);
  //     }else if(k == 1){
  //       return sign*cosMaclaurin(x);
  //     }else if(k == 2){
  //       return sign*-sinMaclaurin(x);
  //     }else{
  //       return sign*-cosMaclaurin(x);
  //     }
  //
  // }
  //
  //
  // private static float cosMaclaurin(float x){
  //   /*Cosinus function using Maclaurin series and FMA*/
  //   float C1 =  0.5f;
  //   float C2 =  1/24f;
  //   float C3 =  1/720f;
  //   float C4 =  1/40320f;
  //   float C5 =  1/3628800f;
  //   float C6 =  1/479001600f;
  //   float C7 =  1/87178291200f;
  //   float s = x * x;
  //   float r = C7;
  //   r = Math.fma(r, -s, C6);
  //   r = Math.fma(r, -s, C5);
  //   r = Math.fma(r, -s, C4);
  //   r = Math.fma(r, -s, C3);
  //   r = Math.fma(r, -s, C2);
  //   r = Math.fma(r, -s, C1);
  //   r = Math.fma(r, -s, 1);
  //   return r;
  // }
  //
  // private static float sinMaclaurin(float x){
  //   /*Sinus function using Maclaurin series and FMA*/
  //   float C1 =  1/6f;
  //   float C2 =  1/120f;
  //   float C3 =  1/5040f;
  //   float C4 =  1/362880f;
  //   float C5 =  1/39916800f;
  //   float C6 =  1/6227020800f;
  //   float C7 =  1/1307674368000f;
  //   float s = x * x;
  //   float r = C7;
  //   r = Math.fma(r, -s, C6);
  //   r = Math.fma(r, -s, C5);
  //   r = Math.fma(r, -s, C4);
  //   r = Math.fma(r, -s, C3);
  //   r = Math.fma(r, -s, C2);
  //   r = Math.fma(r, -s, C1);
  //   r = Math.fma(r, -s, 1);
  //   return x*r;
  // }
//
//   public static float reduction1(float x){
//     /* Return x reduced in [0, pi/2] thanks to cody and waite */
//     int k = (int) (x / HALFPI);
//     x = (x- k*C3) - k * C4;
//     return x;
// }
//
  // public static float reduction2(float x){
  //   /* Return x reduced in [0, 2*pi] thanks to cody and waite */
  //   int k = (int) (x / TWOPI);
  //   x = (x - k*C1) - k * C2; //Get rid of value larger than 2PI
  //   return x;
  // }

  public static float reduction3(float x){
    /* Return x reduced in [0, pi/4] thanks to cody and waite */
    int k = (int) (2*x / HALFPI); // x mod pi/4
    float C1 = 0.78515625f; // representable en machine
    float C2 = 0.00024187564849853515624f; //représentable en machine
    float C3 = 0.000000037747668102383613583f;
    float C4 = 0.00000000000128167207614641725f;
    x = (((x - k*C1) - k * C2) - k * C3) - k* C4; // x mod (C=C1+C2+C3+C4)
    return x;
  }


  //https://stackoverflow.com/questions/30463616/payne-hanek-algorithm-implementation-in-c
//http://www.microchip.com/stellent/groups/techpub_sg/documents/appnotes/en010982.pdf
public static float cos (float x){
    if(x < 0){
      x = -x;
    }
    int k = (int) (2*x / HALFPI); //x mod pi/4
    k = k % 8;
    x = reduction3(x);
    float coeff = 1.0f/1.414213562373095f;
		if(k==1){
			return (float)(coeff*(cos_poly(x) - sin_poly(x)));
		}
		else if (k==2){
			return -sin_poly(x);
		}
		else if (k==3){
			return (float)(-coeff*(cos_poly(x)+sin_poly(x)));
		}
		else if (k==4){
			return -cos_poly(x);
		}
		else if (k==5){
			return (float)(coeff*(-cos_poly(x)+sin_poly(x)));
		}
		else if (k==6){
			return sin_poly(x);
		}
		else if (k==7){
			return (float)(coeff*(cos_poly(x) + sin_poly(x)));
		}else{
      return cos_poly(x);
    }
}
  public static float sin(float x){
    int sign = 1; // sin(-x) = -sin(x)
    if(x < 0){
      x = -x;
      sign = -1;
    }
    int k = (int) (2*x / HALFPI); //x mod pi/4
    k = k % 8;
    x = reduction3(x);
    float coeff = 1.0f/1.414213562373095f;
		if(k==1){
			return (float)(sign*coeff*(sin_poly(x)+cos_poly(x)));
		}
		else if (k==2){
			return sign*cos_poly(x);
		}
		else if (k==3){
			return (float)(sign*coeff*( -sin_poly(x) + cos_poly(x) ) );
		}
		else if (k==4){
			return -sign*sin_poly(x);
		}
		else if (k==5){
			return (float)(-sign*coeff*(sin_poly(x)+cos_poly(x)));
		}
		else if (k==6){
			return -sign*cos_poly(x);
		}
		else if (k==7){
			return (float)(sign*coeff*(sin_poly(x)-cos_poly(x)));
		}else{
      return sign*sin_poly(x);
    }
  }

  public static float cos_poly (float x){
      float r = 0;
      float s = x*x;
      r = 0x1.98e616p-16f;
      r = Math.fma(r, s, -0x1.6c06dcp-10f);
      r = Math.fma(r, s,  0x1.55553cp-05f);
      r = Math.fma(r, s, -0x1.000000p-01f);
      r = Math.fma(r, s,  0x1.000000p+00f);
      return r;
  }

  public static float sin_poly (float x){
    float r = 0;
    float s = x*x;
    r = 0x1.7d3bbcp-19f;
    r = Math.fma(r, s, -0x1.a06bbap-13f);
    r = Math.fma(r, s,  0x1.11119ap-07f);
    r = Math.fma(r, s, -0x1.555556p-03f);
    r = r * s + 0.0f; // ensure -0 is passed trough
    r = Math.fma(r, x, x);
    return r;
}

  // public static float sin(float x){
  //   return cos(HALFPI - x);
  // }


  public static float atan(float x){
    //atan(x) = sign(x)π/2 − atan(1/x)
    int sign = x < 0 ? -1 : 1;
    if (abs(x) >= 1){
      return sign * HALFPI - atan_poly(1/x);
    }else{
      return atan_poly(x);
    }


  }

  private static float atan_poly(float x){
   //We compute the horner form of minimax polynom of atan on [-1,1]
    float s = x * x;
    float r = 0x1.6d2026p-9f;
    r = Math.fma(r, s, -0x1.03f2d4p-6f);
    r = Math.fma(r, s,  0x1.5beeb4p-5f);
    r = Math.fma(r, s, -0x1.33194ep-4f);
    r = Math.fma(r, s,  0x1.b403a8p-4f);
    r = Math.fma(r, s, -0x1.22f5c2p-3f);
    r = Math.fma(r, s,  0x1.997748p-3f);
    r = Math.fma(r, s, -0x1.5554d8p-2f);
    r = r * s;
    return Math.fma(r, x, x);
  }

  public static float asin(float x){
    //arcsine(x)	= atan(x/√(1-x2))
    if(x < -1 || x > 1){
      throw new IllegalArgumentException("Asin function does not accept argument outside [-1,1] !");
    }
    if (x == 1){
      return HALFPI;
    }
    if (x == -1){
      return -HALFPI;
    }
    return atan(x/sqrt(1-x*x));
  }

  public static float acos(float x){
    // arccosine(x)	= Π/2 - arcsine(x)
    if(x < -1 || x > 1){
      throw new IllegalArgumentException("Acos function does not accept argument outside [-1,1] !");
    }
    if (x == 1){
      return 0;
    }
    if (x == -1){
      return PI;
    }
    return HALFPI - asin(x);
  }
}
