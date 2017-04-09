package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
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
        ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoadingRule);
        ApiInterface apiService;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<RuleDto>> getRule = apiService.getRules();
        getRule.enqueue(new Callback<RestService<RuleDto>>() {
            @Override
            public void onResponse(Call<RestService<RuleDto>> call, Response<RestService<RuleDto>> response) {
                pbLoading.setVisibility(View.GONE);
                RuleDto ruleDto = response.body().getData();
                List<BookTypeDto> bookTypeDtos = ruleDto.getListTypeBook();
                List<String> listTypeBook = new ArrayList<String>();
                String rule = "Để thuận tiện cho việc quản lý, và tối ưu quy trình mượn sách, thư viện sẽ thu tiền 'tạm ứng' cho mỗi cuốn sách được mượn. Cách tính tiền cho từng loại sách như sau: \n" ;
                for (int i = 0; i < bookTypeDtos.size(); i++){
                    String a = "- Đối với " + bookTypeDtos.get(i).getName() + ":\n"
                            + "\t• Thời gian mượn tối đa: " + bookTypeDtos.get(i).getBorrowLimitDays() + " ngày.\n"
                            + "\t• Số lần được phép gia hạn: " + bookTypeDtos.get(i).getExtendTimesLimit() + " lần.\n"
                            + "\t• Thời gian được phép trễ sách: " + bookTypeDtos.get(i).getLateDaysLimit() + " ngày.\n";
                    listTypeBook.add(a);
                    rule += a;
                }
                rule += "- Sau khi hết thời gian, nếu không hoàn trả hoặc gia hạn, thư viện sẽ trừ " + ConvertUtils.convertCurrency(ruleDto.getFine_cost()) + " cho từng ngày trễ.\n";
                rule += "- Sau thời gian được phép trễ nếu vẫn không hoàn trả sách cho thư viện, chúng tôi sẽ hiểu rằng bạn mua sách và tiến hành trừ tất cả số tiền bạn ứng trước khỏi tài khoản của bạn.\n";
                tvRule.setText(rule);
            }

            @Override
            public void onFailure(Call<RestService<RuleDto>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
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
