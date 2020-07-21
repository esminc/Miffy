package jp.co.esm.miffy.component;

import ajd4jp.AJD;
import ajd4jp.Holiday;

public class HolidayWrap {
    /**
     * 祝日かどうかを判定する。
     *
     * @param date 祝日判定対象日。
     * @return 祝日ならばtrue、祝日でなければfalseを返す。
     */
    public static boolean isHoliday(AJD date) {
        return Holiday.getHoliday(date) != null;
    }
}
