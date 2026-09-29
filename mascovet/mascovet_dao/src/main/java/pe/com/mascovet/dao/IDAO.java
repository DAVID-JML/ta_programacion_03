package pe.com.mascovet.dao;

import java.util.List;

public interface IDAO <T>{
    int insertar(T objeto);
    int modificar(T objeto);
    int eliminar(int idObjeto);
    T obtenerPorId(int idObjeto);
    List<T> listarTodos();

}
