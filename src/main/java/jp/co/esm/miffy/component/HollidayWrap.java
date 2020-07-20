package jp.co.esm.miffy.component;

import ajd4jp.AJD;
import ajd4jp.Holiday;

import java.time.ZoneId;

import static ajd4jp.iso.AJD310.now;

public class HollidayWrap {
    public static boolean isHoliday(AJD date) {
        return Holiday.getHoliday(date) != null;
    }
}
