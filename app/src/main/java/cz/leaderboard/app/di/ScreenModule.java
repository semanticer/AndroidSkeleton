package cz.leaderboard.app.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import javax.inject.Named;
import cz.leaderboard.app.data.repository.FirebaseLeaderboardRepository;
import cz.leaderboard.app.domain.LeaderboardRepository;
import cz.leaderboard.app.domain.board.AddBoardUseCase;
import cz.leaderboard.app.domain.board.AddScoreUseCase;
import cz.leaderboard.app.domain.board.AddUserUseCase;
import cz.leaderboard.app.domain.board.GetCheckpointsUseCase;
import cz.leaderboard.app.domain.board.GetScoresUseCase;
import cz.leaderboard.app.domain.board.GetTopBoardsUseCase;
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
    static BoardPresenter providePostListPresenter(GetScoresUseCase getScoresUseCase, AddScoreUseCase addScoreUseCase, AddUserUseCase addUserUseCase, GetCheckpointsUseCase getCheckpointsUseCase){
        return new BoardPresenter(getScoresUseCase, addScoreUseCase, addUserUseCase, getCheckpointsUseCase);
    }

    @Provides
    @ScreenScope
    static IntroPresenter provideIntroPresenter(AddBoardUseCase addBoardUseCase, GetTopBoardsUseCase getTopBoardsUseCase){
        return new IntroPresenter(addBoardUseCase, getTopBoardsUseCase);
    }

    @Provides
    @ScreenScope
    static GetTopBoardsUseCase provideGetTopBoardsUseCase(LeaderboardRepository leaderboardRepository){
        return new GetTopBoardsUseCase(leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }

    @Provides
    @ScreenScope
    static GetCheckpointsUseCase provideGetCheckpointsUseCase(LeaderboardRepository leaderboardRepository){
        return new GetCheckpointsUseCase(leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }

    @Provides
    @ScreenScope
    static GetScoresUseCase provideGetLeaderboardUseCase(FirebaseUser firebaseUser, LeaderboardRepository leaderboardRepository){
        return new GetScoresUseCase(firebaseUser, leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }

    @Provides
    @ScreenScope
    static AddUserUseCase provideAddUserUseCase(FirebaseUser firebaseUser, LeaderboardRepository leaderboardRepository){
        return new AddUserUseCase(firebaseUser, leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }


    @Provides
    @ScreenScope
    static AddBoardUseCase provideAddBoardUseCaseUseCase(LeaderboardRepository leaderboardRepository){
        return new AddBoardUseCase(leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
    }

    @Provides
    @ScreenScope
    static AddScoreUseCase provideAddScoreUseCase(FirebaseUser user, LeaderboardRepository leaderboardRepository){
        return new AddScoreUseCase(user, leaderboardRepository, Schedulers::newThread, AndroidSchedulers::mainThread);
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
