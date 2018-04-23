/* 190 bits of 2/pi for Payne-Hanek style argument reduction. */
static const unsigned int two_over_pi_f [] =
{
    0x00000000,
    0x28be60db,
    0x9391054a,
    0x7f09d5f4,
    0x7d4d3770,
    0x36d8a566,
    0x4f10e410
};

float trig_red_slowpath_f (float a, int *quadrant)
{
    unsigned long long int p;
    unsigned int ia, hi, mid, lo, tmp, i;
    int e, q;
    float r;

    ia = (unsigned int)(fabsf (frexpf (a, &e)) * 0x1.0p32f);

    /* extract 96 relevant bits of 2/pi based on magnitude of argument */
    i = (unsigned int)e >> 5;
    e = (unsigned int)e & 31;

    hi  = two_over_pi_f [i+0];
    mid = two_over_pi_f [i+1];
    lo  = two_over_pi_f [i+2];
    tmp = two_over_pi_f [i+3];

    if (e) {
        hi  = (hi  << e) | (mid >> (32 - e));
        mid = (mid << e) | (lo  >> (32 - e));
        lo  = (lo  << e) | (tmp >> (32 - e));
    }

    /* compute product x * 2/pi in 2.62 fixed-point format */
    p = (unsigned long long int)ia * lo;
    p = (unsigned long long int)ia * mid + (p >> 32);
    p = ((unsigned long long int)(ia * hi) << 32) + p;

    /* round quotient to nearest */
    q = (int)(p >> 62);                // integral portion = quadrant<1:0>
    p = p & 0x3fffffffffffffffULL;     // fraction
    if (p & 0x2000000000000000ULL) {   // fraction >= 0.5
        p = p - 0x4000000000000000ULL; // fraction - 1.0
        q = q + 1;
    }

    /* compute remainder of x / (pi/2) */
    double d;

    d = (double)(long long int)p;
    d = d * 0x1.921fb54442d18p-62; // 1.5707963267948966 * 0x1.0p-62
    r = (float)d;
    if (a < 0.0f) {
        r = -r;
        q = -q;
    }

    *quadrant = q;
    return r;
}

/* Like rintf(), but -0.0f -> +0.0f, and |a| must be <= 0x1.0p+22 */
float quick_and_dirty_rintf (float a)
{
    float cvt_magic = 0x1.800000p+23f;
    return (a + cvt_magic) - cvt_magic;
}

/* Argument reduction for trigonometric functions that reduces the argument
   to the interval [-PI/4, +PI/4] and also returns the quadrant. It returns
   -0.0f for an input of -0.0f
*/
float trig_red_f (float a, float switch_over, int *q)
{
    float j, r;

    if (fabsf (a) > switch_over) {
        /* Payne-Hanek style reduction. M. Payne and R. Hanek, Radian reduction
           for trigonometric functions. SIGNUM Newsletter, 18:19-24, 1983
        */
        r = trig_red_slowpath_f (a, q);
    } else {
        /* FMA-enhanced Cody-Waite style reduction. W. J. Cody and W. Waite,
           "Software Manual for the Elementary Functions", Prentice-Hall 1980
        */
        j = 0x1.45f306p-1f * a;             // 2/pi
        j = quick_and_dirty_rintf (j);
        r = fmaf (j, -0x1.921fb0p+00f, a);  // pio2_high
        r = fmaf (j, -0x1.5110b4p-22f, r);  // pio2_mid
        r = fmaf (j, -0x1.846988p-48f, r);  // pio2_low
        *q = (int)j;
    }
    return r;
}

/* Approximate sine on [-PI/4,+PI/4]. Maximum ulp error = 0.64721
   Returns -0.0f for an argument of -0.0f
   Polynomial approximation based on unpublished work by T. Myklebust
*/
float sinf_poly (float a, float s)
{
    float r;

    r =              0x1.7d3bbcp-19f;
    r = fmaf (r, s, -0x1.a06bbap-13f);
    r = fmaf (r, s,  0x1.11119ap-07f);
    r = fmaf (r, s, -0x1.555556p-03f);
    r = r * s + 0.0f; // ensure -0 is passed trough
    r = fmaf (r, a, a);
    return r;
}

/* Approximate cosine on [-PI/4,+PI/4]. Maximum ulp error = 0.87531 */
float cosf_poly (float s)
{
    float r;

    r =              0x1.98e616p-16f;
    r = fmaf (r, s, -0x1.6c06dcp-10f);
    r = fmaf (r, s,  0x1.55553cp-05f);
    r = fmaf (r, s, -0x1.000000p-01f);
    r = fmaf (r, s,  0x1.000000p+00f);
    return r;
}

/* Map sine or cosine value based on quadrant */
float sinf_cosf_core (float a, int i)
{
    float r, s;

    s = a * a;
    r = (i & 1) ? cosf_poly (s) : sinf_poly (a, s);
    if (i & 2) {
        r = 0.0f - r; // don't change "sign" of NaNs
    }
    return r;
}

/* maximum ulp error = 1.49241 */
float my_sinf (float a)
{
    float r;
    int i;

    a = a * 0.0f + a; // inf -> NaN
    r = trig_red_f (a, 117435.992f, &i);
    r = sinf_cosf_core (r, i);
    return r;
}

/* maximum ulp error = 1.49510 */
float my_cosf (float a)
{
    float r;
    int i;

    a = a * 0.0f + a; // inf -> NaN
    r = trig_red_f (a, 71476.0625f, &i);
    r = sinf_cosf_core (r, i + 1);
    return r;
}
