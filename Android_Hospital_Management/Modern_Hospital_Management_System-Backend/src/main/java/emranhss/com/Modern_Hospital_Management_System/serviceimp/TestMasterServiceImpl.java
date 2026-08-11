package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.TestMasterMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.TestMasterRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.TestMasterResponse;

import emranhss.com.Modern_Hospital_Management_System.entity.TestMaster;
import emranhss.com.Modern_Hospital_Management_System.repository.TestMasterRepository;
import emranhss.com.Modern_Hospital_Management_System.service.TestMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestMasterServiceImpl implements TestMasterService {

    private final TestMasterRepository repository;
    private final TestMasterMapper testMasterMapper;

    @Override
    public TestMasterResponse save(TestMasterRequest request) {

        TestMaster test = new TestMaster();

        test.setTestCode(request.getTestCode());
        test.setTestName(request.getTestName());
        test.setStandardPrice(request.getStandardPrice());
        test.setNormalRange(request.getNormalRange());
        test.setActive(true);

        TestMaster saved = repository.save(test);

        return testMasterMapper.toResponse(saved);
    }

    @Override
    public TestMasterResponse update(Long id, TestMasterRequest request) {

        TestMaster test = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Test not found with id: " + id));

        test.setTestCode(request.getTestCode());
        test.setTestName(request.getTestName());
        test.setStandardPrice(request.getStandardPrice());
        test.setNormalRange(request.getNormalRange());

        TestMaster updated = repository.save(test);

        return testMasterMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        TestMaster test = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Test not found with id: " + id));

        test.setActive(false);

        repository.save(test);
    }

    @Override
    public TestMasterResponse getById(Long id) {

        TestMaster test = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Test not found with id: " + id));

        return testMasterMapper.toResponse(test);
    }

    @Override
    public List<TestMasterResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(testMasterMapper::toResponse)
                .toList();
    }

    @Override
    public List<TestMasterResponse> search(String keyword) {

        return repository
                .findByTestNameContainingIgnoreCaseOrTestCodeContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(testMasterMapper::toResponse)
                .toList();
    }


}
