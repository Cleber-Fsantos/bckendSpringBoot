package com.senai.apivsconnect.controllers;

import com.senai.apivsconnect.dtos.UsuarioDto;
import com.senai.apivsconnect.models.UsuarioModel;
import com.senai.apivsconnect.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/usuarios", produces = {"application/json"})
public class UsuarioController {
    @Autowired

    UsuarioRepository usuarioRepository;


    //Mapping de GETS
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    @GetMapping("/{idusuario}")
    public ResponseEntity<Object> exibirUsuario(@PathVariable(value = "idusuario") UUID id){
       Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

       if (usuarioBuscado.isEmpty()){
           //Retorna usuário não encontrado
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
       }

       return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }

    //Mapping de POST
    @PostMapping
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDto){
        //Antes de Cadastrar é preciso consultar se o email existe // Antes Criar uma camada de DTO
        if (usuarioRepository.findByEmail(usuarioDto.email()) != null){
            //Não pode cadastrar caso tenha algum email
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esse email já está cadastrado!");
        }
        //Cria um novo objeto usuario
        UsuarioModel usuario = new UsuarioModel();
        //Copia as propriedades do Usuario DTO para o novo usuario criado
        BeanUtils.copyProperties(usuarioDto, usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    @PutMapping("/{idusuario}")
    public ResponseEntity<Object> editarUsuario(@PathVariable(value = "idusuario") UUID id, @RequestBody @Valid UsuarioDto usuarioDto){
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado!");
        }

        //Cria um novo objeto usuario
        UsuarioModel usuario = usuarioBuscado.get();
        //Copia as propriedades do Usuario DTO para o novo usuario criado
        BeanUtils.copyProperties(usuarioDto, usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        usuarioRepository.delete(usuarioBuscado.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuario deletado com sucesso!");
    }

}
