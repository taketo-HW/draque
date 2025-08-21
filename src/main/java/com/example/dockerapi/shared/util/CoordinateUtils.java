package com.example.dockerapi.shared.util;

/**
 * 座標計算のユーティリティクラス
 */
public class CoordinateUtils {

    /**
     * 2点間の距離を計算する
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * 2点間の距離が指定された範囲内かチェックする
     */
    public static boolean isWithinRange(double x1, double y1, double x2, double y2, double range) {
        return calculateDistance(x1, y1, x2, y2) <= range;
    }

    /**
     * 座標が有効な範囲内かチェックする
     */
    public static boolean isValidCoordinate(double x, double y, double minX, double minY, double maxX, double maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    /**
     * 座標を正規化する（0-1の範囲に収める）
     */
    public static double normalizeCoordinate(double value, double min, double max) {
        if (max == min) {
            return 0.0;
        }
        return (value - min) / (max - min);
    }

    /**
     * 角度をラジアンに変換する
     */
    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    /**
     * ラジアンを角度に変換する
     */
    public static double radiansToDegrees(double radians) {
        return radians * 180.0 / Math.PI;
    }

    /**
     * 2点間の角度を計算する
     */
    public static double calculateAngle(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.atan2(deltaY, deltaX);
    }
}
