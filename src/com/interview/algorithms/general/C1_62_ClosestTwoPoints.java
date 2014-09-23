package com.interview.algorithms.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stefanie
 * Date: 9/23/14
 * Time: 2:09 PM
 *
 * Solution: to avoid un-useful distance calculation, using p.x to split the searching area into left part and right part.
 *  the closest point pair should be in left part, or right part, or the points which x with 2 * min-dis(left, right) with center of spliter.
 *         splitter
 *    x1 x2  |  x3 x4    the min dis of left is d1 and min dis of right is d2, do extra searching on the points lay in splitter +/- min(d1, d2).
 *
 * when x in a very large range, and y in a smaller range, it will omit lots of distance calculation since dis is more close to x.
 */
public class C1_62_ClosestTwoPoints {
    static int distanceCount = 0;

    public static Point[] closest(Point[] points) {
        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if (p1.x > p2.x) return 1;
                else if (p1.x < p2.x) return -1;
                else return 0;
            }
        });

        Point[] closest = new Point[2];
        closest(points, 0, points.length - 1, closest);
        return closest;
    }

    private static double closest(Point[] points, int low, int high, Point[] closest) {
        //only two point left
        if (high - low == 1) {
            closest[0] = points[low];
            closest[1] = points[high];
            return distance(points[low], points[high]);
        }
        //split using the center of endpoint points
        int split = (points[high].x + points[low].x) / 2;
        //find the left and right range: (low, left) and (right, high)
        int left = low;
        while(points[left + 1].x < split) left++;
        int right = left + 1;
        //to avoid single node goes to closest function
        if(left == low) left++;
        if(right == high) right--;

        Point[] closetLeft = new Point[2];
        double disLeft = closest(points, low, left, closetLeft);
        Point[] closetRight = new Point[2];
        double disRight = closest(points, right, high, closetRight);

        //find the min dis and points
        double disMin = disLeft < disRight ? disLeft : disRight;
        closest[0] = disLeft < disRight ? closetLeft[0] : closetRight[0];
        closest[1] = disLeft < disRight ? closetLeft[1] : closetRight[1];

        //find the points lay in splitter +/- min(d1, d2), update the min dis and points if more closer points found
        List<Integer> candidates = candidate(points, left, right, split, disMin);
        for (int i = 0; i < candidates.size(); i++) {
            for (int j = i + 1; j < candidates.size(); j++) {
                double ten = distance(points[i], points[j]);
                if (ten < disMin) {
                    closest[0] = points[i];
                    closest[1] = points[j];
                    disMin = ten;
                }
            }
        }
        return disMin;
    }

    private static List<Integer> candidate(Point[] points, int left, int right, int center, double dis) {
        List<Integer> candidates = new ArrayList<Integer>();
        while (left >= 0) {
            if (center - points[left].x <= dis) candidates.add(left--);
            else break;
        }
        while (right < points.length) {
            if (points[right].x - center <= dis) candidates.add(right++);
            else break;
        }
        return candidates;
    }

    public static Point[] correct(Point[] points) {
        Point[] closest = new Point[2];
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double ten = distance(points[i], points[j]);
                if (ten < min) {
                    closest[0] = points[i];
                    closest[1] = points[j];
                    min = ten;
                }
            }
        }
        return closest;
    }

    private static double distance(Point i, Point j) {
        distanceCount++;
        return Math.sqrt(Math.pow((i.y - j.y), 2) + Math.pow((i.x - j.x), 2));
    }
}
