package com.epimorphismmc.monomorphism.math;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.joml.Vector3f;

import java.util.concurrent.ThreadLocalRandom;

import static com.gregtechceu.gtceu.api.GTValues.RNG;

/**
 * Utility class for math operations.
 * <p>
 * Commonly used math Utilities：
 * {@link Mth},
 * {@link org.joml.Math},
 * {@link com.google.common.primitives.Ints},
 * {@link com.google.common.primitives.Longs}
 *
 * @author GateGuardian
 * @date : 2024/5/24
 */
public class MOMath {

    public static final double PI2 = Math.PI * 2;

    @Deprecated(since = "gtm@1.3.2", forRemoval = true) // TODO
    public static int[] split(long value) {
        IntArrayList result = new IntArrayList();
        while (value > 0) {
            int intValue = (int) Math.min(value, Integer.MAX_VALUE);
            result.add(intValue);
            value -= intValue;
        }
        return result.toIntArray();
    }

    public static int nextInt(int minimum, int maximum) {
        return Mth.nextInt(RNG, minimum, maximum);
    }

    public static float nextFloat(float minimum, float maximum) {
        return Mth.nextFloat(RNG, minimum, maximum);
    }

    public static double nextDouble(double minimum, double maximum) {
        return Mth.nextDouble(RNG, minimum, maximum);
    }

    public static Vector3f randomSpherePoint(double x0, double y0, double z0, Vec3 radius) {
        double u = RNG.nextDouble();
        double v = RNG.nextDouble();
        double theta = PI2 * u;
        double phi = Math.acos(2 * v - 1);
        double x = x0 + (radius.x * Math.sin(phi) * Math.cos(theta));
        double y = y0 + (radius.y * Math.sin(phi) * Math.sin(theta));
        double z = z0 + (radius.z * Math.cos(phi));
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public static Vec2 randomCirclePoint(float radius) {
        double u = RNG.nextDouble();
        double theta = PI2 * u;
        double x = (radius * Math.cos(theta));
        double y = (radius * Math.sin(theta));
        return new Vec2((float) x, (float) y);
    }

    public static Vec2 randomRectanglePoint(Vec2 p1, Vec2 p2) {
        float minX = Math.min(p1.x, p2.x);
        float minY = Math.min(p1.y, p2.y);
        float maxX = Math.max(p1.x, p2.x);
        float maxY = Math.max(p1.y, p2.y);

        float randomX = minX + RNG.nextFloat() * (maxX - minX);
        float randomY = minY + RNG.nextFloat() * (maxY - minY);

        return new Vec2(randomX, randomY);
    }

    public static ThreadLocalRandom localRandom() {
        return ThreadLocalRandom.current();
    }
}
