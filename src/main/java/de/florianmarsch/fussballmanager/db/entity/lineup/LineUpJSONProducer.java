package de.florianmarsch.fussballmanager.db.entity.lineup;

import de.florianmarsch.fussballmanager.db.json.AbstractJSONProducer;

public class LineUpJSONProducer extends AbstractJSONProducer<LineUp>{

	public LineUpJSONProducer() {
		
		
		
		super(new LineUpService());
	}

}
