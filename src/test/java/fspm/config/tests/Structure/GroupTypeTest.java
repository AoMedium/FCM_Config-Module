package fspm.config.tests.Structure;

import org.junit.Test;

import fspm.config.params.groups.DocumentCategoryNameGroup;
import fspm.config.params.groups.DocumentHybridCategoryNameGroup;

import static fspm.config.ConfigTestSuite.*;

public class GroupTypeTest {
    @Test
    // @Ignore
    public void testGroupTypes() {
        DocumentCategoryNameGroup group = CONFIG.getGroup(
                "model.input.data.default", DocumentCategoryNameGroup.class);
        println(group);

        DocumentHybridCategoryNameGroup hybridGroup = CONFIG.getGroup(
                "hybrid-format", DocumentHybridCategoryNameGroup.class);
        println(hybridGroup);

        // ParamTable soilPhysicalProperties =
        // hybridGroup.getTableHierarchy().getTable("soilPhysicalProperties");
        // println(soilPhysicalProperties.getValue("layer_1",
        // "layerThickness"));
    }
}
