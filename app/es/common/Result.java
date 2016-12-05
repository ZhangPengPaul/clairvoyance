package es.common;

import com.google.gson.Gson;
import com.huxin.soa.es.po.AggsPo;
import lombok.Data;

import java.util.List;

/**
 * TODO {file desc}
 *
 * @author GaoJian
 * @version 0.1
 * @since 16/4/6 上午11:36
 */
@Data
public class Result {

	private boolean ok; // true表示批量全部成功，false 相反
	private String error; // 失败信息

	private List<AggsPo> aggsList;

	private List<>

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
