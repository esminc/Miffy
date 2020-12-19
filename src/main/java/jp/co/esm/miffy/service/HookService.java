package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.Holiday;
import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HookService {
    public final Asf4MemberRepository asf4MemberRepository;

    /**
     * テーブルのデータ一覧を返す。
     *
     * @return List型でメンバ一覧を返す。
     */
    public List<Asf4Member> selectAll() {
        return asf4MemberRepository.findAllByOrderByIdAsc();
    }

    /**
     * 前回の掃除当番の人を特定する。
     *
     * @return 前回の掃除当番をAsf4Memberクラスで返す。
     */
    private Asf4Member getLastCleaner() {
        Optional<Asf4Member> lastCleanerOptional = asf4MemberRepository.findByCleanerTrue();
        return lastCleanerOptional.orElseThrow(() -> new NoSuchElementException("IsCleaner == true に一致する情報がありません。"));
    }

    /**
     * 今日の掃除当番の人を特定する。
     *
     * @return 掃除当番をOptionalオブジェクトで返す。
     */
    private Asf4Member getCleaner() throws NoSuchElementException {
        Asf4Member lastCleaner = getLastCleaner();
        int cleanerId = lastCleaner.getId();
        Optional<Asf4Member> cleanerOptional = asf4MemberRepository.findTopBySkipFalseAndIdGreaterThanOrderByIdAsc(cleanerId);
        if (cleanerOptional.isEmpty()) {
            cleanerOptional = asf4MemberRepository.findTopBySkipFalseOrderByIdAsc();
        }
        Asf4Member cleaner;
        if (cleanerOptional.isPresent()) {
            cleaner = cleanerOptional.get();
            cleaner.setCleaner(true);
            asf4MemberRepository.saveAndFlush(cleaner);
            lastCleaner.setCleaner(false);
            asf4MemberRepository.saveAndFlush(lastCleaner);
        } else {
            cleaner = null;
        }
        return cleaner;
    }

    /**
     * 祝日かどうかを判定する。
     *
     * @param date 祝日判定対象日。
     * @return 祝日ならばtrue、祝日でなければfalseを返す。
     */
    private boolean isHoliday(AJD date) {
        return Holiday.getHoliday(date) != null;
    }

    /**
     * 現在の掃除当番情報を取得する
     *
     * skip == false かつ cleaner == true に一致するのが今日の掃除当番
     * 条件に一致するメンバーがいないときは NoSuchElementExceptionを投げる
     * @return 今日の掃除当番をOptional型で返す
     */
    Asf4Member getCurrentCleaner() {
        Optional<Asf4Member> currentCleanerOptional = asf4MemberRepository.findByCleanerTrueAndSkipFalse();
        return currentCleanerOptional.orElseThrow(() -> new NoSuchElementException("cleaner == true かつ skip == false に一致する情報がありません。"));
    }

    /**
     * あとでJSONにする通知メッセージ本文を作成する
     *
     * @return メンション用テキスト、本文、を結合して String で返す
     */
    String makeMainMessage() {
        String postIdobataId = null;
        String errorMessage = " ";
        String mainMessage = null;
        StringBuilder messageTotal = new StringBuilder();
        Asf4Member cleaner;
        try {
            cleaner = getCurrentCleaner();
            if (cleaner != null) {
                postIdobataId = cleaner.getIdobataId();
                mainMessage = "今日の掃除当番です";
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            postIdobataId = "here ";
            errorMessage = e.getMessage();  // 9:00am時点でcleaner==trueがいないまたは全員がskip==trueのとき
            mainMessage = "今日はみんなお掃除できないみたい(・x・)";
        }
        messageTotal.append(postIdobataId);
        messageTotal.append(errorMessage);
        messageTotal.append(mainMessage);
        return messageTotal.toString();
    }

    /**
     * 曜日・日付に応じたメンション付きの掃除当番通知用リクエスト文を生成する。
     * 該当するメンバーが誰もいない場合は本文にその旨を記載する。
     *
     * @return hookのURLへPOSTリクエストするJSON形式テキストを返す。祝日はnullを返す。
     */
    public String makeRequest(AJD date) {
        if (isHoliday(date)) {
            return null;
        }
        String mainMessage = makeMainMessage();
//        Asf4Member cleaner;
//        try {
//            cleaner = getCleaner();
//            if (cleaner != null) {
//                postIdobataId = cleaner.getIdobataId();
//                mainMessage = "今日の掃除当番です";
//            } else {
//                postIdobataId = "here";
//                mainMessage = "今日は誰もオフィスにいないみたい(・x・)";
//            }
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//            postIdobataId = "all ";
//            errorMessage = e.getMessage();
//            mainMessage = "前回掃除した人は誰？(・x・)";
//        }
        StringBuilder request = new StringBuilder();
        request.append("{");
        request.append("\"source\":\"");
        request.append("@");
        request.append(mainMessage);
        request.append("\"");
        request.append("}");
        return request.toString();
    }
}


