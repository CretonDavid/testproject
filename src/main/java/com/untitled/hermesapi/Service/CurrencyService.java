package com.untitled.hermesapi.Service;

import com.untitled.hermesapi.dto.currency.CurrencyRequest;
import com.untitled.hermesapi.dto.currency.CurrencyResponse;
import com.untitled.hermesapi.exception.ResourceAlreadyExistsException;
import com.untitled.hermesapi.exception.ResourceNotFoundException;
import com.untitled.hermesapi.mapper.CurrencyMapper;
import com.untitled.hermesapi.model.Currency;
import com.untitled.hermesapi.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyResponse create(CurrencyRequest request) {
        //if (request == null) throw new BadRequestException("Request body cannot be null");
        String code = request.code().toUpperCase();
        if (currencyRepository.findByCode((code)).isPresent()) {
            throw new ResourceAlreadyExistsException("Currency with code '" + code + "' already exists");
        }

        Currency currency = currencyMapper.toEntity(request);
        Currency saved = currencyRepository.save(currency);
        log.info("A new currency has been created with name : {}", currency.getName());

        return currencyMapper.toResponse(saved);
    }

    public CurrencyResponse update(Long id, CurrencyRequest request) {
        //if (request == null) throw new ApplicationException("Request body cannot be null");
        Currency existing = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));

        String code = request.code().toUpperCase();
        currencyRepository.findByCode(code).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new ResourceAlreadyExistsException("Currency with code '" + code + "' already exists");
            }
        });

        existing.setCode(code);
        existing.setName(request.name());
        existing.setSymbol(request.symbol());
        Currency saved = currencyRepository.save(existing);
        log.info("Currency has been modified : {}", saved.getName());

        return currencyMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found with id " + id);
        }
        currencyRepository.deleteById(id);
        log.info("Currency with id {} has been deleted", id);
    }

    public List<CurrencyResponse> findAll() {
        return currencyRepository.findAll()
                .stream()
                .map(currencyMapper::toResponse)
                .toList();
    }
}
