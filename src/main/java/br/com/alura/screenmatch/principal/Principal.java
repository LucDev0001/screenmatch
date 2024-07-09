package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=50fb4dfa";
    private final List<DadosSerie> dadosSeries = new ArrayList<>();

    public void exibeMenu() {
        int opcao = -1;
        while (opcao != 0) {
            String menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1 -> buscarSerieWeb();
                case 2 -> buscarEpisodioPorSerie();
                case 3 -> listarSeriesBuscadas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        if (dados != null) {
            dadosSeries.add(dados);
            System.out.println(dados);
        } else {
            System.out.println("Não foi possível encontrar a série.");
        }
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        String nomeSerie = leitura.nextLine();
        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        if (dadosSerie != null) {
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
                String json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
        } else {
            System.out.println("Não foi possível encontrar a série.");
        }
    }

    private void listarSeriesBuscadas() {
        if (dadosSeries.isEmpty()) {
            System.out.println("Nenhuma série buscada.");
        } else {
            dadosSeries.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        Principal principal = new Principal();
        principal.exibeMenu();
    }
}

