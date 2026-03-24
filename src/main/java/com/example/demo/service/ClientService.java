package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    @AuditAction(action = "CREATION_CLIENT", entityName = "Client")
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        return clientMapper.toDTO(clientRepository.save(client));
    }

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "MISE_A_JOUR_CLIENT", entityName = "Client")
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    Client updatedClient = clientMapper.toEntity(clientDTO);
                    updatedClient.setId(existingClient.getId());
                    return clientMapper.toDTO(clientRepository.save(updatedClient));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "SUPPRESSION_CLIENT", entityName = "Client")
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client not found with ID: " + id);
        }
        clientRepository.deleteById(id);
    }
}
