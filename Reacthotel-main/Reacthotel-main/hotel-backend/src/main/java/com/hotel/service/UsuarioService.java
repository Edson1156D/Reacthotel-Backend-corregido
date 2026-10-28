package com.hotel.service;

import com.hotel.dto.UsuarioCreate;
import com.hotel.dto.UsuarioResponse;
import com.hotel.dto.UsuarioUpdate;
import com.hotel.entity.Rol;
import com.hotel.entity.Usuario;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.repository.MantenimientoRepository;
import com.hotel.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MantenimientoRepository mantenimientoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            MantenimientoRepository mantenimientoRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponse::desde)
                .toList();
    }

    public UsuarioResponse crear(UsuarioCreate request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictoException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol() == null ? Rol.RECEPCION : request.rol());
        usuario.setActivo(request.activo() == null || request.activo());
        return UsuarioResponse.desde(usuarioRepository.save(usuario));
    }

    public UsuarioResponse actualizar(Long id, UsuarioUpdate request) {
        Usuario usuario = buscar(id);

        if (request.email() != null
                && !request.email().equals(usuario.getEmail())
                && usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictoException("El email ya está registrado");
        }

        if (request.nombre() != null) {
            usuario.setNombre(request.nombre());
        }
        if (request.email() != null) {
            usuario.setEmail(request.email());
        }
        if (request.rol() != null) {
            usuario.setRol(request.rol());
        }
        if (request.activo() != null) {
            usuario.setActivo(request.activo());
        }

        return UsuarioResponse.desde(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        Usuario usuario = buscar(id);
        if (mantenimientoRepository.existsByUsuario_Id(usuario.getId())) {
            throw new ConflictoException(
                    "No se puede eliminar el usuario porque tiene mantenimientos asignados");
        }
        usuarioRepository.delete(usuario);
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }
}
