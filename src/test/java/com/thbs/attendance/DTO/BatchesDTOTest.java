package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchesDTOTest {

    @Test
    public void testBatchesDTOConstructor() {
        // Test data
        long batchId = 1L;
        String batchName = "Batch 1";

        // Create BatchesDTO object using constructor
        BatchesDTO batchesDTO = new BatchesDTO(batchId, batchName);

        // Verify data
        assertEquals(batchId, batchesDTO.getBatchId());
        assertEquals(batchName, batchesDTO.getBatchName());
    }

    @Test
    public void testBatchesDTOSetterGetter() {
        // Test data
        long batchId = 1L;
        String batchName = "Batch 1";

        // Create BatchesDTO object
        BatchesDTO batchesDTO = new BatchesDTO();

        // Set data using setters
        batchesDTO.setBatchId(batchId);
        batchesDTO.setBatchName(batchName);

        // Verify data using getters
        assertEquals(batchId, batchesDTO.getBatchId());
        assertEquals(batchName, batchesDTO.getBatchName());
    }
}
