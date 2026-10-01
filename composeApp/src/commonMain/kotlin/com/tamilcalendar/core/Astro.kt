package com.tamilcalendar.core

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

/**
 * Compact astronomical engine (Jean Meeus, "Astronomical Algorithms").
 * Moon: ELP-2000/82 truncated series (ch. 47) – accuracy ~10" ; Sun: ch. 25 – ~0.01°.
 * That is enough for panchangam end-times within ~1-2 minutes.
 */
object Astro {
    private const val DEG = PI / 180.0
    private const val DELTA_T_SECONDS = 69.5 // TT - UT for 2026

    fun norm360(x: Double): Double { val r = x % 360.0; return if (r < 0) r + 360.0 else r }
    fun norm180(x: Double): Double { val r = norm360(x); return if (r > 180) r - 360 else r }

    private fun centuriesTT(jdUt: Double): Double = (jdUt + DELTA_T_SECONDS / 86400.0 - 2451545.0) / 36525.0

    /** Apparent tropical longitude of the Sun (degrees). */
    fun sunLongitude(jdUt: Double): Double {
        val t = centuriesTT(jdUt)
        val l0 = 280.46646 + 36000.76983 * t + 0.0003032 * t * t
        val m = (357.52911 + 35999.05029 * t - 0.0001537 * t * t) * DEG
        val c = (1.914602 - 0.004817 * t - 0.000014 * t * t) * sin(m) +
            (0.019993 - 0.000101 * t) * sin(2 * m) + 0.000289 * sin(3 * m)
        val omega = (125.04 - 1934.136 * t) * DEG
        return norm360(l0 + c - 0.00569 - 0.00478 * sin(omega))
    }

    // D, M, M', F, coefficient (1e-6 deg)
    private val MOON_TERMS = intArrayOf(
        0, 0, 1, 0, 6288774, 2, 0, -1, 0, 1274027, 2, 0, 0, 0, 658314,
        0, 0, 2, 0, 213618, 0, 1, 0, 0, -185116, 0, 0, 0, 2, -114332,
        2, 0, -2, 0, 58793, 2, -1, -1, 0, 57066, 2, 0, 1, 0, 53322,
        2, -1, 0, 0, 45758, 0, 1, -1, 0, -40923, 1, 0, 0, 0, -34720,
        0, 1, 1, 0, -30383, 2, 0, 0, -2, 15327, 0, 0, 1, 2, -12528,
        0, 0, 1, -2, 10980, 4, 0, -1, 0, 10675, 0, 0, 3, 0, 10034,
        4, 0, -2, 0, 8548, 2, 1, -1, 0, -7888, 2, 1, 0, 0, -6766,
        1, 0, -1, 0, -5163, 1, 1, 0, 0, 4987, 2, -1, 1, 0, 4036,
        2, 0, 2, 0, 3994, 4, 0, 0, 0, 3861, 2, 0, -3, 0, 3665,
        0, 1, -2, 0, -2689, 2, 0, -1, 2, -2602, 2, -1, -2, 0, 2390,
        1, 0, 1, 0, -2348, 2, -2, 0, 0, 2236, 0, 1, 2, 0, -2120,
        0, 2, 0, 0, -2069, 2, -2, -1, 0, 2048, 2, 0, 1, -2, -1773,
        2, 0, 0, 2, -1595, 4, -1, -1, 0, 1215, 0, 0, 2, 2, -1110,
        3, 0, -1, 0, -892, 2, 1, 1, 0, -810, 4, -1, -2, 0, 759,
        0, 2, -1, 0, -713, 2, 2, -1, 0, -700, 2, 1, -2, 0, 691,
        2, -1, 0, -2, 596, 4, 0, 1, 0, 549, 0, 0, 4, 0, 537,
        4, -1, 0, 0, 520, 1, 0, -2, 0, -487, 2, 1, 0, -2, -399,
        0, 0, 2, -2, -381, 1, 1, 1, 0, 351, 3, 0, -2, 0, -340,
        4, 0, -3, 0, 330, 2, -1, 2, 0, 327, 0, 2, 1, 0, -323,
        1, 1, -1, 0, 299,
    )

    /** Apparent (approx.) tropical longitude of the Moon (degrees). */
    fun moonLongitude(jdUt: Double): Double {
        val t = centuriesTT(jdUt)
        val t2 = t * t; val t3 = t2 * t; val t4 = t3 * t
        val lp = 218.3164477 + 481267.88123421 * t - 0.0015786 * t2 + t3 / 538841.0 - t4 / 65194000.0
        val d = (297.8501921 + 445267.1114034 * t - 0.0018819 * t2 + t3 / 545868.0 - t4 / 113065000.0) * DEG
        val m = (357.5291092 + 35999.0502909 * t - 0.0001536 * t2 + t3 / 24490000.0) * DEG
        val mp = (134.9633964 + 477198.8675055 * t + 0.0087414 * t2 + t3 / 69699.0 - t4 / 14712000.0) * DEG
        val f = (93.2720950 + 483202.0175233 * t - 0.0036539 * t2 - t3 / 3526000.0 + t4 / 863310000.0) * DEG
        val e = 1.0 - 0.002516 * t - 0.0000074 * t2
        var sum = 0.0
        var i = 0
        while (i < MOON_TERMS.size) {
            val cd = MOON_TERMS[i]; val cm = MOON_TERMS[i + 1]; val cmp = MOON_TERMS[i + 2]; val cf = MOON_TERMS[i + 3]
            var coef = MOON_TERMS[i + 4].toDouble()
            if (abs(cm) == 1) coef *= e else if (abs(cm) == 2) coef *= e * e
            sum += coef * sin(cd * d + cm * m + cmp * mp + cf * f)
            i += 5
        }
        val a1 = (119.75 + 131.849 * t) * DEG
        val a2 = (53.09 + 479264.290 * t) * DEG
        sum += 3958 * sin(a1) + 1962 * sin(lp * DEG - f) + 318 * sin(a2)
        val omega = (125.04452 - 1934.136261 * t) * DEG
        val nutation = -0.004778 * sin(omega) // dominant nutation-in-longitude term
        return norm360(lp + sum / 1_000_000.0 + nutation)
    }

    /** Lahiri (Chitrapaksha) ayanamsa in degrees. */
    fun ayanamsa(jdUt: Double): Double {
        val t = centuriesTT(jdUt)
        return 23.85309 + 1.396971 * t + 0.000308 * t * t
    }

    fun siderealSun(jd: Double) = norm360(sunLongitude(jd) - ayanamsa(jd))
    fun siderealMoon(jd: Double) = norm360(moonLongitude(jd) - ayanamsa(jd))

    // ----- Sun rise / set -------------------------------------------------

    private fun sunDeclinationAndRa(jdUt: Double): Pair<Double, Double> {
        val t = centuriesTT(jdUt)
        val lambda = sunLongitude(jdUt) * DEG
        val eps = obliquity(t) * DEG
        val dec = asin(sin(eps) * sin(lambda))
        val ra = atan2(cos(eps) * sin(lambda), cos(lambda))
        return dec to norm360(ra / DEG)
    }

    private fun obliquity(t: Double) = 23.439291 - 0.0130042 * t

    /** Greenwich mean sidereal time in degrees. */
    fun gmst(jdUt: Double): Double {
        val t = (jdUt - 2451545.0) / 36525.0
        return norm360(280.46061837 + 360.98564736629 * (jdUt - 2451545.0) + 0.000387933 * t * t)
    }

    /**
     * Sunrise (rising = true) or sunset of the given IST civil date, as JD (UT).
     * Uses the upper limb with standard refraction (-0.833°). Iterative.
     */
    fun sunEvent(date: SimpleDate, loc: Location, rising: Boolean): Double {
        var jd = date.jdMidnightIst + (if (rising) 6.0 else 18.0) / 24.0
        repeat(5) {
            val (dec, ra) = sunDeclinationAndRa(jd)
            val phi = loc.latitude * DEG
            val h0 = -0.833 * DEG
            val cosH = (sin(h0) - sin(phi) * sin(dec)) / (cos(phi) * cos(dec))
            val hDeg = acos(cosH.coerceIn(-1.0, 1.0)) / DEG
            val lst = norm360(gmst(jd) + loc.longitude)
            val currentHa = norm180(lst - ra)
            val targetHa = if (rising) -hDeg else hDeg
            val diff = norm180(targetHa - currentHa)
            jd += diff / 360.98564736629
        }
        return jd
    }

    /** Sidereal (nirayana) ascendant longitude. */
    fun ascendant(jdUt: Double, loc: Location): Double {
        val t = centuriesTT(jdUt)
        val eps = obliquity(t) * DEG
        val ramc = norm360(gmst(jdUt) + loc.longitude) * DEG
        val phi = loc.latitude * DEG
        val asc = atan2(cos(ramc), -(sin(ramc) * cos(eps) + tan(phi) * sin(eps))) / DEG
        return norm360(asc - ayanamsa(jdUt))
    }

    /**
     * Finds the JD when [angle] (a continuously increasing angular quantity, degrees)
     * reaches [target] (mod 360), starting from [startJd]. [rate] is the approx deg/day.
     */
    fun findAngle(startJd: Double, target: Double, rate: Double, angle: (Double) -> Double): Double {
        var jd = startJd + norm360(target - angle(startJd)) / rate
        repeat(30) {
            val diff = norm180(target - angle(jd))
            if (abs(diff) < 1e-6) return jd
            jd += diff / rate
        }
        return jd
    }

    fun floorInt(x: Double): Int = floor(x).toInt()
}

data class Location(val name: String, val latitude: Double, val longitude: Double)

val CHENNAI = Location("Chennai", 13.0827, 80.2707)
