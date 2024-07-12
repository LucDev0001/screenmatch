package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    //metodo para econtrar serie por um nome do titulo.
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    //metodo para buscar serie pelo nome do ator.
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor,  Double avaliacao);

    //metodo buscar top 5 series
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    //metodo para achar por categoria utilizando o atributo genero
    List<Serie> findByGenero(Categoria categoria);

    // motodo buscando serie por total de temporadas e avaliação.
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

    //filtrando series mais utilizando querry para diminuir o nome do metodo.
    //JPQL = LINGUAGEM DE CONCULTA DA JPA = CONTROLE DE PERSISTENCIA DO JAVA.


    @Query( "select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas  AND s.avaliacao >= :avaliacao")
    List<Serie>  seriesPorTemporadasEAvaliacao(int totalTemporadas, double avaliacao);

    //QUERY JPQL BUSCAR EPISODIOS POR TRECHO
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE  e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);


    //QUERY JPQL ORDENANDO TOP 5 EPISODIOS POR SERIE.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Optional<Serie> serie, int anoLancamento);
}