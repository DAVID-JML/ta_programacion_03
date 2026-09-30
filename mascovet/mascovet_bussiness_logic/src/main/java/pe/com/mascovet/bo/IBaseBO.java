package pe.com.mascovet.bo;

import java.util.List;

public interface IBaseBO<T> {
    int insertar(T objeto) throws RuntimeException;
    int modificar(T objeto) throws RuntimeException;
    int eliminar(int idObjeto) throws RuntimeException;
    List<T> listarTodos() throws RuntimeException;
    T obtenerPorId(int idObjeto) throws RuntimeException;
}
