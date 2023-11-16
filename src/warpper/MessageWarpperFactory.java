package warpper;
/** 

* @author  Donghao.F 

* @date 2020 Dec 14 12:24:00 

* 

*/


import com.kedong.platform.imdg.nio.serialization.DataSerializableFactory;
import com.kedong.platform.imdg.nio.serialization.IdentifiedDataSerializable;

import app.model.data.AddtionModelData;
import app.model.preset.process.ModelPointValueGetEntryProcess;
import app.model.preset.process.ModelPointValueSetEntryProcess;
import app.model.table.task.TableTask;
import app.model.task.PointValueTask;

public class MessageWarpperFactory implements DataSerializableFactory{

	@Override
	public IdentifiedDataSerializable create(int classId) {
		switch (classId) {
		case 100:
//			return PhysicalModelFactory.eINSTANCE.createPModelContainer();
		case 101:
			return new MessageWarpper();
		case 102:
			return new PointValueTask();
		case 103:
			return new TableTask();
		case 104:
			return new ModelPointValueSetEntryProcess();
		case 105:
			return new ModelPointValueGetEntryProcess();
		case 106:
			return new AddtionModelData();
		default:
			return null;
		}
	}

}
