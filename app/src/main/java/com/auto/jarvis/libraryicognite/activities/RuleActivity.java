package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.BookTypeDto;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.models.output.RuleDto;
import com.auto.jarvis.libraryicognite.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thiendn on 08/04/2017.
 */

public class RuleActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        TextView tvRule = (TextView) findViewById(R.id.tvRule);
        ApiInterface apiService;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<RuleDto>> getRule = apiService.getRules();
        getRule.enqueue(new Callback<RestService<RuleDto>>() {
            @Override
            public void onResponse(Call<RestService<RuleDto>> call, Response<RestService<RuleDto>> response) {
                RuleDto ruleDto = response.body().getData();
                List<BookTypeDto> bookTypeDtos = ruleDto.getListTypeBook();
                List<String> listTypeBook = new ArrayList<String>();
                String rule = "";
                for (int i = 0; i < bookTypeDtos.size(); i++){
                    String a = "•\tĐối với " + bookTypeDtos.get(i).getName() + ":\n"
                            + "•\tThời gian mượn tối đa là " + bookTypeDtos.get(i).getBorrowLimitDays() + "ngày.\n"
                            + "•\tSố lần được phép gia hạn " + bookTypeDtos.get(i).getExtendTimesLimit() + "ngày.\n"
                            + "•\tSau khi hết thời gian, nếu không hoàn trả hoặc gia hạn, thư viện sẽ trừ tiền " + ConvertUtils.convertCurrency(ruleDto.getFine_cost()) + " cho từng ngày trễ.\n"
                            + "•\tSau " + bookTypeDtos.get(i).getLateDaysLimit() + " nếu vẫn không hoàn trả sách cho thư viện, chúng tôi sẽ xem như bạn mua sách và tiến hành trừ số tiền bạn ứng trước khỏi tài khoản của bạn.\n";
                    listTypeBook.add(a);
                    rule += a;
                }
                tvRule.setText(rule);
            }

            @Override
            public void onFailure(Call<RestService<RuleDto>> call, Throwable t) {
                tvRule.setText("Can not connect to server!");
            }
        });
//        System.out.println("rule activity start");
        String rule = "\tĐể được mượn sách, số tiền trong tài khoản phải lớn hơn hoặc bằng số tiền cọc được quy định cho từng cuốn\n"
                + "•\tĐối với sách tham khảo:\n"
                + "•\tThời gian mượn tối đa là [borrow_limit_days].\n"
                + "•\tSố lần được phép gia hạn [extend_times_limit].\n"
                + "•\tSau khi hết thời gian, nếu không hoàn trả hoặc gia hạn, thư viện sẽ trừ tiền [tien phat mot ngay] cho từng ngày trễ.\n"
                + "•\tSau [late_days_limit] nếu vẫn không hoàn trả sách cho thư viện, chúng tôi sẽ xem như bạn mua sách và tiến hành trừ số tiền bạn ứng trước khỏi tài khoản của bạn.\n";
    }
}
