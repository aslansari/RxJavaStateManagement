package com.aslansari.rxjavastatemng.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aslansari.rxjavastatemng.R;
import com.aslansari.rxjavastatemng.network.GithubService;
import com.aslansari.rxjavastatemng.network.ServiceGenerator;
import com.aslansari.rxjavastatemng.ui.SubmitAction;
import com.aslansari.rxjavastatemng.ui.SubmitEvent;
import com.aslansari.rxjavastatemng.ui.SubmitResult;
import com.aslansari.rxjavastatemng.ui.SubmitUiModel;
import com.jakewharton.rxbinding3.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.buttonSubmit)
    Button button;

    GithubService githubService;
    CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        disposables = new CompositeDisposable();

        githubService = ServiceGenerator.createService(GithubService.class,GithubService.BASE_URL);

        SubmitUiModel initialState = SubmitUiModel.idle();

        ObservableTransformer<SubmitAction, SubmitResult> submit = actions -> actions
                .flatMap(action -> githubService.getUser(action.name)
                        .map(SubmitResult::success)
                        .onErrorReturn(t -> SubmitResult.failure(t.getMessage()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(SubmitResult.inProgress()));

        disposables.add(RxView.clicks(button)
                .map(ignored -> new SubmitEvent(editTextName.getText().toString()))
                .map(event -> new SubmitAction(event.username))
                .compose(submit)
                .scan(initialState, (state,result) -> {
                    if (result.inProgress) return SubmitUiModel.inProgress();
                    if (result.success) return SubmitUiModel.success(result.user.getName());
                    if (result.error) return SubmitUiModel.failure(result.errorMessage);
                    else return SubmitUiModel.idle();
//                    throw new IllegalArgumentException("Unknown result: " + result);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    button.setEnabled(!model.inProgress);
                    progressBar.setVisibility(model.inProgress ? View.VISIBLE : View.GONE);
                    if (!model.inProgress){
                        if (model.success) textView.setText(model.errorMessage);
                        else Toast.makeText(getApplicationContext(),"Failed: "+ model.errorMessage,Toast.LENGTH_LONG).show();
                    }
                },throwable -> {throw new OnErrorNotImplementedException(throwable); }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
