package com.hotel.service;

import com.hotel.dto.ClienteRequest;
import com.hotel.dto.ClienteResponse;
import com.hotel.dto.ClienteUpdate;
import com.hotel.entity.Cliente;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.ClienteRepository;
import com.hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            ReservaRepository reservaRepository) {
        this.clienteRepository = clienteRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponse::desde)
                .toList();
    }

    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setApellido(request.apellido());
        cliente.setDni(request.dni());
        cliente.setEmail(request.email());
        cliente.setTelefono(request.telefono());
        cliente.setDireccion(request.direccion());
        return ClienteResponse.desde(clienteRepository.save(cliente));
    }

    public ClienteResponse actualizar(Long id, ClienteUpdate request) {
        Cliente cliente = buscar(id);

        if (request.isNombrePresente()) {
            cliente.setNombre(validarTexto(request.getNombre(), "El nombre no puede estar vacío"));
        }
        if (request.isApellidoPresente()) {
            cliente.setApellido(validarTexto(request.getApellido(), "El apellido no puede estar vacío"));
        }
        if (request.isDniPresente()) {
            cliente.setDni(validarTexto(request.getDni(), "El DNI no puede estar vacío"));
        }
        if (request.isEmailPresente()) {
            cliente.setEmail(request.getEmail());
        }
        if (request.isTelefonoPresente()) {
            cliente.setTelefono(request.getTelefono());
        }
        if (request.isDireccionPresente()) {
            cliente.setDireccion(request.getDireccion());
        }

        return ClienteResponse.desde(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        Cliente cliente = buscar(id);
        if (reservaRepository.existsByClienteId(cliente.getId())) {
            throw new ConflictoException(
                    "No se puede eliminar: el cliente tiene reservas asociadas");
        }
        clienteRepository.delete(cliente);
    }

    private Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }

    private String validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new SolicitudInvalidaException(mensaje);
        }
        return valor;
    }
}
