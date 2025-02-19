package org.knowm.xchange.kraken.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class KrakenTradeServiceTest extends BaseWiremockTest {

    private KrakenTradeService classUnderTest;

    @Before
    public void setup() {
        classUnderTest = (KrakenTradeService) createExchange().getTradeService();
    }

    @Test
    public void ordersTest() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonRoot = mapper.readTree(OPEN_ORDERS_BODY);

        stubFor(
                post(urlPathEqualTo("/0/private/OpenOrders"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(OPEN_ORDERS_BODY)
                        )
        );

        OpenOrders openOrders = classUnderTest.getOpenOrders();

        assertThat(openOrders).isNotNull();
        assertThat(openOrders.getOpenOrders()).hasSize(jsonRoot.get("result").
                get("open").size());
        LimitOrder firstOrder = openOrders.getOpenOrders().get(0);
        assertThat(firstOrder).isNotNull();
        assertThat(firstOrder.getOriginalAmount()).isNotNull().isPositive();
        assertThat(firstOrder.getId()).isNotBlank();
        assertThat(firstOrder.getInstrument()).isEqualTo(CurrencyPair.BTC_USD);

        LimitOrder secondOrder = openOrders.getOpenOrders().get(1);
        assertThat(secondOrder).isNotNull();
        assertThat(secondOrder.getOriginalAmount()).isNotNull().isPositive();
        assertThat(secondOrder.getId()).isNotBlank();
        assertThat(secondOrder.getInstrument()).isEqualTo(CurrencyPair.LTC_USD);

    }

    @Test
    public void tradeHistoryTest() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonRoot = mapper.readTree(ORDER_HISTORY_BODY);

        stubFor(
                post(urlPathEqualTo("/0/private/TradesHistory"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(ORDER_HISTORY_BODY)
                        )
        );

        TradeHistoryParams tradeHistoryParams = classUnderTest.createTradeHistoryParams();

        UserTrades userTrades = classUnderTest.getTradeHistory(tradeHistoryParams);

        assertThat(userTrades).isNotNull();
        assertThat(userTrades.getUserTrades()).hasSize(jsonRoot.get("result").
                get("trades").size());
        UserTrade firstUserTrade = userTrades.getUserTrades().get(0);
        assertThat(firstUserTrade).isNotNull();
        assertThat(firstUserTrade.getOriginalAmount()).isNotNull().isPositive();
        assertThat(firstUserTrade.getId()).isNotBlank();
        assertThat(firstUserTrade.getInstrument()).isEqualTo(CurrencyPair.BTC_USD);

        UserTrade secondUserTrade = userTrades.getUserTrades().get(1);
        assertThat(secondUserTrade).isNotNull();
        assertThat(secondUserTrade.getOriginalAmount()).isNotNull().isPositive();
        assertThat(secondUserTrade.getId()).isNotBlank();
        assertThat(secondUserTrade.getInstrument()).isEqualTo(CurrencyPair.LTC_USD);
    }

    private static String OPEN_ORDERS_BODY = "{\n" +
            "  \"error\": [],\n" +
            "  \"result\": {\n" +
            "    \"open\": {\n" +
            "      \"OQCLML-BW3P3-BUCMWZ\": {\n" +
            "        \"refid\": null,\n" +
            "        \"userref\": 0,\n" +
            "        \"status\": \"open\",\n" +
            "        \"opentm\": 1616666559.8974,\n" +
            "        \"starttm\": 0,\n" +
            "        \"expiretm\": 0,\n" +
            "        \"descr\": {\n" +
            "          \"pair\": \"XBTUSD\",\n" +
            "          \"type\": \"buy\",\n" +
            "          \"ordertype\": \"limit\",\n" +
            "          \"price\": \"30010.0\",\n" +
            "          \"price2\": \"0\",\n" +
            "          \"leverage\": \"none\",\n" +
            "          \"order\": \"buy 1.25000000 XBTUSD @ limit 30010.0\",\n" +
            "          \"close\": \"\"\n" +
            "        },\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"vol_exec\": \"0.37500000\",\n" +
            "        \"cost\": \"11253.7\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"price\": \"30010.0\",\n" +
            "        \"stopprice\": \"0.00000\",\n" +
            "        \"limitprice\": \"0.00000\",\n" +
            "        \"misc\": \"\",\n" +
            "        \"oflags\": \"fciq\",\n" +
            "        \"trades\": [\n" +
            "          \"TCCCTY-WE2O6-P3NB37\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"OB5VMB-B4U2U-DK2WRW\": {\n" +
            "        \"refid\": null,\n" +
            "        \"userref\": 120,\n" +
            "        \"status\": \"open\",\n" +
            "        \"opentm\": 1616665899.5699,\n" +
            "        \"starttm\": 0,\n" +
            "        \"expiretm\": 0,\n" +
            "        \"descr\": {\n" +
            "          \"pair\": \"LTCUSD\",\n" +
            "          \"type\": \"buy\",\n" +
            "          \"ordertype\": \"limit\",\n" +
            "          \"price\": \"14500.0\",\n" +
            "          \"price2\": \"0\",\n" +
            "          \"leverage\": \"5:1\",\n" +
            "          \"order\": \"buy 0.27500000 LTCUSD @ limit 14500.0 with 5:1 leverage\",\n" +
            "          \"close\": \"\"\n" +
            "        },\n" +
            "        \"vol\": \"0.27500000\",\n" +
            "        \"vol_exec\": \"0.00000000\",\n" +
            "        \"cost\": \"0.00000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"price\": \"0.00000\",\n" +
            "        \"stopprice\": \"0.00000\",\n" +
            "        \"limitprice\": \"0.00000\",\n" +
            "        \"misc\": \"\",\n" +
            "        \"oflags\": \"fciq\"\n" +
            "      },\n" +
            "      \"OXHXGL-F5ICS-6DIC67\": {\n" +
            "        \"refid\": null,\n" +
            "        \"userref\": 120,\n" +
            "        \"status\": \"open\",\n" +
            "        \"opentm\": 1616665894.0036,\n" +
            "        \"starttm\": 0,\n" +
            "        \"expiretm\": 0,\n" +
            "        \"descr\": {\n" +
            "          \"pair\": \"LTCUSD\",\n" +
            "          \"type\": \"buy\",\n" +
            "          \"ordertype\": \"limit\",\n" +
            "          \"price\": \"17500.0\",\n" +
            "          \"price2\": \"0\",\n" +
            "          \"leverage\": \"5:1\",\n" +
            "          \"order\": \"buy 0.27500000 LTCUSD @ limit 17500.0 with 5:1 leverage\",\n" +
            "          \"close\": \"\"\n" +
            "        },\n" +
            "        \"vol\": \"0.27500000\",\n" +
            "        \"vol_exec\": \"0.00000000\",\n" +
            "        \"cost\": \"0.00000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"price\": \"0.00000\",\n" +
            "        \"stopprice\": \"0.00000\",\n" +
            "        \"limitprice\": \"0.00000\",\n" +
            "        \"misc\": \"\",\n" +
            "        \"oflags\": \"fciq\"\n" +
            "      },\n" +
            "      \"OLQCVY-B27XU-MBPCL5\": {\n" +
            "        \"refid\": null,\n" +
            "        \"userref\": 251,\n" +
            "        \"status\": \"open\",\n" +
            "        \"opentm\": 1616665556.7646,\n" +
            "        \"starttm\": 0,\n" +
            "        \"expiretm\": 0,\n" +
            "        \"descr\": {\n" +
            "          \"pair\": \"XBTUSD\",\n" +
            "          \"type\": \"buy\",\n" +
            "          \"ordertype\": \"limit\",\n" +
            "          \"price\": \"23500.0\",\n" +
            "          \"price2\": \"0\",\n" +
            "          \"leverage\": \"none\",\n" +
            "          \"order\": \"buy 0.27500000 XBTUSD @ limit 23500.0\",\n" +
            "          \"close\": \"\"\n" +
            "        },\n" +
            "        \"vol\": \"0.27500000\",\n" +
            "        \"vol_exec\": \"0.00000000\",\n" +
            "        \"cost\": \"0.00000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"price\": \"0.00000\",\n" +
            "        \"stopprice\": \"0.00000\",\n" +
            "        \"limitprice\": \"0.00000\",\n" +
            "        \"misc\": \"\",\n" +
            "        \"oflags\": \"fciq\"\n" +
            "      },\n" +
            "      \"OQCGAF-YRMIQ-AMJTNJ\": {\n" +
            "        \"refid\": null,\n" +
            "        \"userref\": 0,\n" +
            "        \"status\": \"open\",\n" +
            "        \"opentm\": 1616665511.0373,\n" +
            "        \"starttm\": 0,\n" +
            "        \"expiretm\": 0,\n" +
            "        \"descr\": {\n" +
            "          \"pair\": \"XBTUSD\",\n" +
            "          \"type\": \"buy\",\n" +
            "          \"ordertype\": \"limit\",\n" +
            "          \"price\": \"24500.0\",\n" +
            "          \"price2\": \"0\",\n" +
            "          \"leverage\": \"none\",\n" +
            "          \"order\": \"buy 1.25000000 XBTUSD @ limit 24500.0\",\n" +
            "          \"close\": \"\"\n" +
            "        },\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"vol_exec\": \"0.00000000\",\n" +
            "        \"cost\": \"0.00000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"price\": \"0.00000\",\n" +
            "        \"stopprice\": \"0.00000\",\n" +
            "        \"limitprice\": \"0.00000\",\n" +
            "        \"misc\": \"\",\n" +
            "        \"oflags\": \"fciq\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static String ORDER_HISTORY_BODY = "{\n" +
            "  \"error\": [],\n" +
            "  \"result\": {\n" +
            "    \"trades\": {\n" +
            "      \"THVRQM-33VKH-UCI7BS\": {\n" +
            "        \"ordertxid\": \"OQCLML-BW3P3-BUCMWZ\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616667796.8802,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30010.00000\",\n" +
            "        \"cost\": \"600.20000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.02000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TCWJEG-FL4SZ-3FKGH6\": {\n" +
            "        \"ordertxid\": \"OQCLML-BW3P3-BUCMWZ\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616667769.6396,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30010.00000\",\n" +
            "        \"cost\": \"300.10000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.01000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TCCCTY-WE2O6-P3NB37\": {\n" +
            "        \"ordertxid\": \"OQCLML-BW3P3-BUCMWZ\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616666586.6077,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30010.00000\",\n" +
            "        \"cost\": \"11253.75000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.37500000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TZX2WP-XSEOP-FP7WYR\": {\n" +
            "        \"ordertxid\": \"OBCMZD-JIEE7-77TH3F\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616665496.7842,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30021.00000\",\n" +
            "        \"cost\": \"37526.25000\",\n" +
            "        \"fee\": \"37.52500\",\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TJUW2K-FLX2N-AR2FLU\": {\n" +
            "        \"ordertxid\": \"OMMDB2-FSB6Z-7W3HPO\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616592012.2334,\n" +
            "        \"type\": \"sell\",\n" +
            "        \"ordertype\": \"market\",\n" +
            "        \"price\": \"30000.00000\",\n" +
            "        \"cost\": \"7500.00000\",\n" +
            "        \"fee\": \"7.50000\",\n" +
            "        \"vol\": \"0.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TKTFJU-O5OIY-63ZBF4\": {\n" +
            "        \"ordertxid\": \"OFWNMX-VIOHE-HCYVU6\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616591594.5669,\n" +
            "        \"type\": \"sell\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30000.00000\",\n" +
            "        \"cost\": \"15000.00000\",\n" +
            "        \"fee\": \"15.00000\",\n" +
            "        \"vol\": \"0.50000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TRJPJP-6VYIP-5XLJZA\": {\n" +
            "        \"ordertxid\": \"O3XCSX-SLY3X-LGHPI3\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616522002.4217,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30014.00000\",\n" +
            "        \"cost\": \"37517.50000\",\n" +
            "        \"fee\": \"37.51592\",\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TSALPW-Q3HPI-LEPCOA\": {\n" +
            "        \"ordertxid\": \"O6355P-ESPHJ-PEXFBY\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616521990.7427,\n" +
            "        \"type\": \"sell\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30000.00000\",\n" +
            "        \"cost\": \"150000.00000\",\n" +
            "        \"fee\": \"150.00000\",\n" +
            "        \"vol\": \"5.00000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TQ7WSQ-U3R7T-ZUMFIY\": {\n" +
            "        \"ordertxid\": \"ONZOB7-3ESLV-IQ2PZT\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616521380.6707,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30013.43386\",\n" +
            "        \"cost\": \"37516.79233\",\n" +
            "        \"fee\": \"37.51606\",\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T42BLA-LGJHI-LPVTTD\": {\n" +
            "        \"ordertxid\": \"O3DZBO-5SI2M-S6WCNU\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616521336.9991,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30013.00000\",\n" +
            "        \"cost\": \"37516.25000\",\n" +
            "        \"fee\": \"37.51597\",\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TUI2JG-VOE36-SW7UJQ\": {\n" +
            "        \"ordertxid\": \"OZABVF-MIK6V-L3ZTOE\",\n" +
            "        \"postxid\": \"TF5GVO-T7ZZ2-6NBKBI\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616511385.1402,\n" +
            "        \"type\": \"sell\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30000.00000\",\n" +
            "        \"cost\": \"60.00000\",\n" +
            "        \"fee\": \"0.06000\",\n" +
            "        \"vol\": \"0.00200000\",\n" +
            "        \"margin\": \"12.00000\",\n" +
            "        \"misc\": \"closing\"\n" +
            "      },\n" +
            "      \"TXSFI3-5CTX5-LPTJHK\": {\n" +
            "        \"ordertxid\": \"OICKON-6IMOR-2ODO5A\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616511195.1372,\n" +
            "        \"type\": \"sell\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30000.00000\",\n" +
            "        \"cost\": \"60.00000\",\n" +
            "        \"fee\": \"0.06000\",\n" +
            "        \"vol\": \"0.00200000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TDRIF6-7SGNJ-IAXFCN\": {\n" +
            "        \"ordertxid\": \"OL5EF7-LFZFJ-QBFM2A\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616492377.7001,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30012.00000\",\n" +
            "        \"cost\": \"37515.00000\",\n" +
            "        \"fee\": \"37.51383\",\n" +
            "        \"vol\": \"1.25000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T2PT3E-H6OGF-F5UABV\": {\n" +
            "        \"ordertxid\": \"ONVNZL-MHDOA-CES7XV\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616008928.0982,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30008.50000\",\n" +
            "        \"cost\": \"15004.25000\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.50000000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TJPO67-7TNED-Y2BRWM\": {\n" +
            "        \"ordertxid\": \"ONVNZL-MHDOA-CES7XV\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616006978.5925,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30008.00000\",\n" +
            "        \"cost\": \"68058.14400\",\n" +
            "        \"fee\": \"68.06066\",\n" +
            "        \"vol\": \"2.26800000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TYYGJC-KEK4Q-L42ZQ4\": {\n" +
            "        \"ordertxid\": \"OG45VQ-ZYQT7-TGLEZR\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZUSD\",\n" +
            "        \"time\": 1616005993.5273,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"30006.53702\",\n" +
            "        \"cost\": \"135164.44600\",\n" +
            "        \"fee\": \"135.16765\",\n" +
            "        \"vol\": \"4.50450000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TKK7S7-ZMT6O-AGOP6D\": {\n" +
            "        \"ordertxid\": \"OKZ4TV-6ZJRL-6X2WTO\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2458,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.17229\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00017212\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TP5XQL-ZN5PG-L6VSNV\": {\n" +
            "        \"ordertxid\": \"OAU5VR-4IAXN-SJW5YB\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2436,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TTIWU5-NQ4VO-JC5BQN\": {\n" +
            "        \"ordertxid\": \"OJ3BNG-YDA52-MDMETR\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2412,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TMBTRV-FTDNZ-P3HUV5\": {\n" +
            "        \"ordertxid\": \"O5X65H-KXHCL-QCLYCT\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2388,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TY44ZN-AIYOJ-SDR7GK\": {\n" +
            "        \"ordertxid\": \"OEKLOP-RDAOE-FZWGKN\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2367,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T5NRGS-AKQZV-ZHG4E4\": {\n" +
            "        \"ordertxid\": \"OICCXO-CRHEQ-LT67QB\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.234,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T7CBB7-AQ5O7-P5GQSK\": {\n" +
            "        \"ordertxid\": \"O7ABWG-VU27X-YP2XDI\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2312,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TENBB5-7BZEQ-TNBE66\": {\n" +
            "        \"ordertxid\": \"O4AIPT-GX3QH-HZGYCH\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2283,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TVLXY3-C6N3I-T25754\": {\n" +
            "        \"ordertxid\": \"OW4Z6S-DSUGW-DFTKBT\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.226,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TJH4MR-DOYCR-5555SK\": {\n" +
            "        \"ordertxid\": \"OGHH7F-C74EX-7GAM4K\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2234,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TXQ36N-GN7IZ-5PKY7L\": {\n" +
            "        \"ordertxid\": \"OPVBIW-AJXET-VNXDJ7\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.221,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T6LBB6-IFMBI-4YPXBX\": {\n" +
            "        \"ordertxid\": \"OKOPXR-U4D6F-GBS4KJ\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2183,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T4M6VU-EECSP-MELY5T\": {\n" +
            "        \"ordertxid\": \"OJI43X-GINSU-QWE6DK\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2157,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TQA5B5-EZLVE-KTM4M5\": {\n" +
            "        \"ordertxid\": \"O4AARU-PTKL7-HSPOEL\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2128,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TKSIRP-ZUYPW-62FLMV\": {\n" +
            "        \"ordertxid\": \"O6MGUZ-XVWWN-GJC3A2\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2104,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TUEHX7-FFLNW-JMFAR7\": {\n" +
            "        \"ordertxid\": \"OY2YWA-4BNZ2-2VMP4Z\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2079,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TJKYBR-A5AYL-24MXFU\": {\n" +
            "        \"ordertxid\": \"OZ45GG-Y7SGJ-NR4B5A\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2052,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TRKPR4-GWUOY-CHPNRN\": {\n" +
            "        \"ordertxid\": \"OL2F4Z-4WGHJ-HMMCJ2\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614110456.2021,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.11299\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00011288\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T37GPE-X26FG-5ZSIIB\": {\n" +
            "        \"ordertxid\": \"OL2F4Z-4WGHJ-HMMCJ2\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3409,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.08721\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00008712\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TQM3FZ-THK6L-XSHSBX\": {\n" +
            "        \"ordertxid\": \"OVKIE5-LM7NM-NWTF64\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3379,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TVDJWV-BADZ2-CV2THT\": {\n" +
            "        \"ordertxid\": \"O4UMTF-7XT6W-4TEAJ5\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3348,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T3TO6H-GZ3UQ-SFA567\": {\n" +
            "        \"ordertxid\": \"OAUJQY-M2LW7-URI6P4\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3319,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TQVOXK-5MOHP-SI5VAV\": {\n" +
            "        \"ordertxid\": \"OXWORC-FSWRK-VCYRD5\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3292,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"T4H46E-SDKQ2-MN47XT\": {\n" +
            "        \"ordertxid\": \"OWWORE-77UTK-24JC4S\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3268,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TQMMMA-2OWI3-REGYSF\": {\n" +
            "        \"ordertxid\": \"ORVBV5-WMOB2-XIS2O7\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3242,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TJXDW4-C2AL5-MVS4S7\": {\n" +
            "        \"ordertxid\": \"OJQVC6-2DL7T-N2VG6R\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3215,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TW7OM6-3AZPP-KHOCDH\": {\n" +
            "        \"ordertxid\": \"OIY22N-PA6L3-VZRYWY\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3189,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TVLE6Y-7THFG-DEWRL2\": {\n" +
            "        \"ordertxid\": \"OEOV45-VCSQF-6JC62L\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3165,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TTEUX3-HDAAA-RC2RUO\": {\n" +
            "        \"ordertxid\": \"OH76VO-UKWAD-PSBDX6\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3138,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TK5SLP-HK2DZ-HJAQ3P\": {\n" +
            "        \"ordertxid\": \"OVL7YT-LDN4Z-R4O4QD\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XXBTZEUR\",\n" +
            "        \"time\": 1614082549.3108,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TB27WI-UMAET-QOYYZL\": {\n" +
            "        \"ordertxid\": \"ORSERE-GNHBA-UDAJNL\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"LTCBTC\",\n" +
            "        \"time\": 1614082549.3079,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TEFPZR-SAZHO-HCT2LJ\": {\n" +
            "        \"ordertxid\": \"OIMMOC-QVPMC-UA35DQ\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"LTCBTC\",\n" +
            "        \"time\": 1614082549.3051,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TS6BLY-CYHBC-35B2WE\": {\n" +
            "        \"ordertxid\": \"O2MDN6-6Y6IB-TSVEHO\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"LTCUSD\",\n" +
            "        \"time\": 1614082549.3015,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      },\n" +
            "      \"TUBIJ2-LJD26-EW3WKN\": {\n" +
            "        \"ordertxid\": \"OFJVXE-B6PHS-HPNIXU\",\n" +
            "        \"postxid\": \"TKH2SE-M7IF5-CFI7LT\",\n" +
            "        \"pair\": \"XBTUSD\",\n" +
            "        \"time\": 1614082549.2986,\n" +
            "        \"type\": \"buy\",\n" +
            "        \"ordertype\": \"limit\",\n" +
            "        \"price\": \"1001.00000\",\n" +
            "        \"cost\": \"0.20020\",\n" +
            "        \"fee\": \"0.00000\",\n" +
            "        \"vol\": \"0.00020000\",\n" +
            "        \"margin\": \"0.00000\",\n" +
            "        \"misc\": \"\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"count\": 2346\n" +
            "  }\n" +
            "}";
}