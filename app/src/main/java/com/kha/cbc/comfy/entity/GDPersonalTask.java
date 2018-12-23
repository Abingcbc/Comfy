package com.kha.cbc.comfy.entity;

import com.kha.cbc.comfy.greendao.gen.DaoSession;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.model.PersonalTask;
import com.kha.cbc.comfy.model.common.BaseCardModel;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

@Entity
public class GDPersonalTask {

    @Id
    String id;
    String title;
    //使用GreenDao一对多
    @ToMany(referencedJoinProperty = "taskId")
    List<GDPersonalCard> personalCardList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1868782258)
    private transient GDPersonalTaskDao myDao;

    @Generated(hash = 1060337810)
    public GDPersonalTask(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Generated(hash = 1062189464)
    public GDPersonalTask() {
    }

    public GDPersonalTask(PersonalTask personalTask) {
        this.id = personalTask.getId();
        this.title = personalTask.getTitle();
        List<BaseCardModel> cardModelList = personalTask.getCards();
        this.personalCardList = new ArrayList<>();
        for (int i = 0; i < cardModelList.size() - 1; i++) {
            this.personalCardList.add(new GDPersonalCard(cardModelList.get(i)));
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PersonalTask toModel() {
        return new PersonalTask(this.title);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 17320856)
    public List<GDPersonalCard> getPersonalCardList() {
        if (personalCardList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GDPersonalCardDao targetDao = daoSession.getGDPersonalCardDao();
            List<GDPersonalCard> personalCardListNew = targetDao
                    ._queryGDPersonalTask_PersonalCardList(id);
            synchronized (this) {
                if (personalCardList == null) {
                    personalCardList = personalCardListNew;
                }
            }
        }
        return personalCardList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 335706845)
    public synchronized void resetPersonalCardList() {
        personalCardList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1340018206)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGDPersonalTaskDao() : null;
    }

}
