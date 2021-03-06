package sample.Services;

import sample.DAO.InfoDao;
import sample.DBModels.Info;

public class InfoService {

    private InfoDao infoDao = new InfoDao();

    public InfoService() {
    }

    public static Info findInfo(int id) {
        return InfoDao.findById(id);
    }

    public void saveInfo(Info Info) {
        InfoDao.save(Info);
    }

    public void deleteInfo(Info Info) {
        InfoDao.delete(Info);
    }

    public static void updateInfo(Info Info) {
        InfoDao.update(Info);
    }

}
