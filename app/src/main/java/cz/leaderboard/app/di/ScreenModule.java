package cz.leaderboard.app.di;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.database.FirebaseDatabase;
import javax.inject.Named;
import cz.leaderboard.app.data.repository.FirebaseLeaderboardRepository;
import cz.leaderboard.app.domain.LeaderboardRepository;
import cz.leaderboard.app.domain.board.GetScoresUseCase;
import cz.leaderboard.app.presentation.board.BoardPresenter;
import cz.leaderboard.app.presentation.intro.IntroPresenter;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Module
public class ScreenModule {
    private String controllerName;

    public ScreenModule(String controllerName) {
        this.controllerName = controllerName;
    }

    @Provides @ScreenScope @Named("controllerName") String getControllerName() {
        return controllerName;
    }

    @Provides
    @ScreenScope
    static BoardPresenter providePostListPresenter(GetScoresUseCase getScoresUseCase){
        return new BoardPresenter(getScoresUseCase);
    }

    @Provides
    @ScreenScope
    static IntroPresenter provideIntroPresenter(){
        return new IntroPresenter();
    }

    @Provides
    @ScreenScope
    static GetScoresUseCase provideGetLeaderboardUseCase(LeaderboardRepository leaderboardRepository){
        return new GetScoresUseCase(leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }

    @Provides
    @ScreenScope
    static LeaderboardRepository provideLeaderboardRepository(FirebaseDatabase firebaseDatabase){
        return new FirebaseLeaderboardRepository(firebaseDatabase);
    }

    @Provides
    @ScreenScope
    static FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @ScreenScope
    static RxSharedPreferences provideRxSharedPreferences(SharedPreferences sharedPreferences){
        return RxSharedPreferences.create(sharedPreferences);
    }

}
