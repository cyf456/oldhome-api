package com.xydl.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈晨
 * @version V1.0.0
 * @projectName xydl-api
 * @title RandSplitNumUtils
 * @package com.xydl.common.utils
 * @description 红包随机金额算法
 * @date 2020/8/11 10:49
 * @copyright 2020 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
public class RandSplitNumUtils {
    private static final Random random = new Random();

    /**
     * 根据总数分割个数及限定区间进行数据随机处理
     * 数列浮动阀值为0.85
     *
     * @param total    - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min      - 单个数字下限
     * @param max      - 单个数字上限
     * @return - 返回符合要求的数字列表
     */
    public static List<Integer> genRandList(int total, int splitNum, int min, int max) {
        return genRandList(total, splitNum, min, max, 0.85f);
    }

    /**
     * 根据总数分割个数及限定区间进行数据随机处理
     *
     * @param total    - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min      - 单个数字下限
     * @param max      - 单个数字上限
     * @param thresh   - 数列浮动阀值[0.0, 1.0]
     * @return
     */
    public static List<Integer> genRandList(int total, int splitNum, int min, int max, float thresh) {
        assert total >= splitNum * min && total <= splitNum * max;
        assert thresh >= 0.0f && thresh <= 1.0f;
        // 平均分配
        int average = total / splitNum;
        List<Integer> list = new ArrayList<>(splitNum);
        int rest = total - average * splitNum;
        for (int i = 0; i < splitNum; i++) {
            if (i < rest) {
                list.add(average + 1);
            } else {
                list.add(average);
            }
        }
        // 如果浮动阀值为0则不进行数据随机处理
        if (thresh == 0) {
            return list;
        }
        // 根据阀值进行数据随机处理
        for (int i = 0; i < splitNum - 1; i++) {
            int nextIndex = i + 1;
            int itemThis = list.get(i);
            int itemNext = list.get(nextIndex);
            boolean isLt = itemThis < itemNext;
            int rangeThis = isLt ? max - itemThis : itemThis - min;
            int rangeNext = isLt ? itemNext - min : max - itemNext;
            int rangeFinal = (int) Math.ceil(thresh * (Math.min(rangeThis, rangeNext) + 1));
            int randOfRange = random.nextInt(rangeFinal);
            int randRom = isLt ? 1 : -1;
            list.set(i, list.get(i) + randRom * randOfRange);
            list.set(nextIndex, list.get(nextIndex) + randRom * randOfRange * -1);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(genRandList(500, 20, 1, 200));
    }
}
