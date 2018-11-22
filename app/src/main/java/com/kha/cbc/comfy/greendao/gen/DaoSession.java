package com.kha.cbc.comfy.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.entity.GDPersonalCard;

import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig gDPersonalTaskDaoConfig;
    private final DaoConfig gDPersonalCardDaoConfig;

    private final GDPersonalTaskDao gDPersonalTaskDao;
    private final GDPersonalCardDao gDPersonalCardDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        gDPersonalTaskDaoConfig = daoConfigMap.get(GDPersonalTaskDao.class).clone();
        gDPersonalTaskDaoConfig.initIdentityScope(type);

        gDPersonalCardDaoConfig = daoConfigMap.get(GDPersonalCardDao.class).clone();
        gDPersonalCardDaoConfig.initIdentityScope(type);

        gDPersonalTaskDao = new GDPersonalTaskDao(gDPersonalTaskDaoConfig, this);
        gDPersonalCardDao = new GDPersonalCardDao(gDPersonalCardDaoConfig, this);

        registerDao(GDPersonalTask.class, gDPersonalTaskDao);
        registerDao(GDPersonalCard.class, gDPersonalCardDao);
    }
    
    public void clear() {
        gDPersonalTaskDaoConfig.clearIdentityScope();
        gDPersonalCardDaoConfig.clearIdentityScope();
    }

    public GDPersonalTaskDao getGDPersonalTaskDao() {
        return gDPersonalTaskDao;
    }

    public GDPersonalCardDao getGDPersonalCardDao() {
        return gDPersonalCardDao;
    }

}