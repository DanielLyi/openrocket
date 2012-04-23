package net.sf.openrocket.preset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.motor.Manufacturer;
import net.sf.openrocket.rocketcomponent.Transition;
import net.sf.openrocket.util.BaseTestCase.BaseTestCase;

import org.junit.Test;

/**
 * Test construction of TRANSITION type ComponentPresets based on TypedPropertyMap through the
 * ComponentPresetFactory.create() method.
 * 
 * Ensure required properties are populated
 * 
 * Ensure any computed values are correctly computed.
 * 
 */
public class TransitionPresetTests extends BaseTestCase {

	@Test
	public void testManufacturerRequired() {
		try {
			TypedPropertyMap presetspec = new TypedPropertyMap();
			presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
			ComponentPresetFactory.create(presetspec);
		} catch ( InvalidComponentPresetException ex ) {
			assertTrue("Wrong Exception Thrown", ex.getMessage().contains("No Manufacturer specified"));
		}
	}

	@Test
	public void testPartNoRequired() {
		try {
			TypedPropertyMap presetspec = new TypedPropertyMap();
			presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
			presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
			ComponentPresetFactory.create(presetspec);
		} catch ( InvalidComponentPresetException ex ) {
			assertTrue("Wrong Exception Thrown", ex.getMessage().contains("No PartNo specified"));
		}
	}

	@Test
	public void testLengthRequired() {
		try {
			TypedPropertyMap presetspec = new TypedPropertyMap();
			presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
			presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
			presetspec.put( ComponentPreset.PARTNO, "partno");
			ComponentPresetFactory.create(presetspec);
		} catch ( InvalidComponentPresetException ex ) {
			assertTrue("Wrong Exception Thrown", ex.getMessage().contains("No Length specified"));
		}
	}

	@Test
	public void testAftOuterDiameterRequired() {
		try {
			TypedPropertyMap presetspec = new TypedPropertyMap();
			presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
			presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
			presetspec.put( ComponentPreset.PARTNO, "partno");
			presetspec.put( ComponentPreset.LENGTH, 2.0);
			presetspec.put( ComponentPreset.SHAPE, Transition.Shape.CONICAL);
			ComponentPresetFactory.create(presetspec);
		} catch ( InvalidComponentPresetException ex ) {
			assertTrue("Wrong Exception Thrown", ex.getMessage().contains("No AftOuterDiameter"));
		}
	}


	@Test
	public void testForeOuterDiameterRequired() {
		try {
			TypedPropertyMap presetspec = new TypedPropertyMap();
			presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
			presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
			presetspec.put( ComponentPreset.PARTNO, "partno");
			presetspec.put( ComponentPreset.LENGTH, 2.0);
			presetspec.put( ComponentPreset.SHAPE, Transition.Shape.CONICAL);
			presetspec.put( ComponentPreset.AFT_OUTER_DIAMETER, 2.0);
			ComponentPresetFactory.create(presetspec);
		} catch ( InvalidComponentPresetException ex ) {
			assertTrue("Wrong Exception Thrown", ex.getMessage().contains("No ForeOuterDiameter"));
		}
	}

	@Test
	public void testComputeDensityNoMaterial() throws Exception {
		TypedPropertyMap presetspec = new TypedPropertyMap();
		presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
		presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
		presetspec.put( ComponentPreset.PARTNO, "partno");
		presetspec.put( ComponentPreset.LENGTH, 2.0);
		presetspec.put( ComponentPreset.SHAPE, Transition.Shape.CONICAL);
		presetspec.put( ComponentPreset.AFT_OUTER_DIAMETER, 2.0);
		presetspec.put( ComponentPreset.FORE_OUTER_DIAMETER, 1.0);
		presetspec.put( ComponentPreset.FILLED, true);
		presetspec.put( ComponentPreset.MASS, 100.0);
		ComponentPreset preset = ComponentPresetFactory.create(presetspec);

		// constants put into the presetspec above.
		double volume = /*base area*/ Math.PI  * ( 1.0 * 1.0 + 1.0 * 0.5 + 0.5 * 0.5);
		
		volume *= 2.0 /* times height */ / 3.0; /* one third */

		double density = 100.0 / volume;
		
		assertEquals("TransitionCustom",preset.get(ComponentPreset.MATERIAL).getName());
		// FIXME - I would expect the nc volume computation to be closer for such a simple shape.
		// simple math yields 27.2837
		// 100/nc.getComponentVolume yields 27.59832
		assertEquals(density,preset.get(ComponentPreset.MATERIAL).getDensity(),0.5);
	}

	@Test
	public void testMaterial() throws Exception {
		TypedPropertyMap presetspec = new TypedPropertyMap();
		presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
		presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
		presetspec.put( ComponentPreset.PARTNO, "partno");
		presetspec.put( ComponentPreset.LENGTH, 2.0);
		presetspec.put( ComponentPreset.SHAPE, Transition.Shape.CONICAL);
		presetspec.put( ComponentPreset.AFT_OUTER_DIAMETER, 2.0);
		presetspec.put( ComponentPreset.FORE_OUTER_DIAMETER, 1.0);
		presetspec.put( ComponentPreset.FILLED, true);
		presetspec.put( ComponentPreset.MATERIAL, new Material.Bulk("test", 2.0, true));
		ComponentPreset preset = ComponentPresetFactory.create(presetspec);

		assertEquals("test",preset.get(ComponentPreset.MATERIAL).getName());
		assertEquals(2.0,preset.get(ComponentPreset.MATERIAL).getDensity(),0.0005);
		
	}

	@Test
	public void testComputeDensityWithMaterial() throws Exception {
		TypedPropertyMap presetspec = new TypedPropertyMap();
		presetspec.put(ComponentPreset.TYPE, ComponentPreset.Type.TRANSITION);
		presetspec.put( ComponentPreset.MANUFACTURER, Manufacturer.getManufacturer("manufacturer"));
		presetspec.put( ComponentPreset.PARTNO, "partno");
		presetspec.put( ComponentPreset.LENGTH, 2.0);
		presetspec.put( ComponentPreset.SHAPE, Transition.Shape.CONICAL);
		presetspec.put( ComponentPreset.AFT_OUTER_DIAMETER, 2.0);
		presetspec.put( ComponentPreset.FORE_OUTER_DIAMETER, 1.0);
		presetspec.put( ComponentPreset.FILLED, true);
		presetspec.put( ComponentPreset.MASS, 100.0);
		presetspec.put( ComponentPreset.MATERIAL, new Material.Bulk("test", 2.0, true));
		ComponentPreset preset = ComponentPresetFactory.create(presetspec);

		// constants put into the presetspec above.
		double totvolume = /*base area*/ Math.PI;
		
		totvolume *= 4.0 /* times height */ / 3.0; /* one third */

		double uppervolume = /*fore area*/ Math.PI * 0.5 * 0.5;
		uppervolume *= 2.0 /* times height */ / 3.0; /* one third */
		
		double volume = totvolume-uppervolume;

		double density = 100.0 / volume;
		
		assertEquals("test",preset.get(ComponentPreset.MATERIAL).getName());
		// FIXME - I would expect the nc volume computation to be closer for such a simple shape.
		assertEquals(density,preset.get(ComponentPreset.MATERIAL).getDensity(),1.5);
	}

}