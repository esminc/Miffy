package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@EnableScheduling
public class hookController {
    private final Asf4MemberService asf4MemberService;

    /**
     * 祝日、休日を除いた月〜金曜日に、idobataのhookを使用して、今日の掃除当番にお知らせをするメソッドです。
     */
    @Scheduled(cron = "0 0 10 * * 1-5", zone = "Asia/Tokyo")
    public void hook() {
        asf4MemberService.postToHook(asf4MemberService.getCleaner().getIdobataId());
    }
}
