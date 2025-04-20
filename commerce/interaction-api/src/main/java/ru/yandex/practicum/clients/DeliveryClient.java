package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.clients.fallback.DeliveryClientFallback;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "delivery", fallback = DeliveryClientFallback.class)
public interface DeliveryClient {

    /**
     * Создание новой заявки на доставку.
     *
     * @param delivery новая заявка на доставку
     * @return указанная заявка с присвоенным идентификатором
     */
    @PutMapping("/api/v1/delivery")
    DeliveryDto createNewDelivery(@RequestBody(required = true) DeliveryDto delivery);

    /**
     * Эмуляция получения товара в доставку.
     * Принять товары в доставку.
     * Изменить статус доставки на IN_PROGRESS.
     * Изменить статус заказа на ASSEMBLED в сервисе заказов
     * Связать идентификатор доставки с внутренней учётной системой (методами сервиса склада)
     * Этот метод вызывается после того, как служба доставки получает заказ-наряд на доставку.
     *
     * @param deliveryId идентификатор доставки
     */
    @PostMapping("/api/v1/delivery/picked")
    DeliveryDto makeDeliveryInProgress(@RequestBody(required = true) String deliveryId);

    /**
     * Эмуляция успешной доставки.
     * Проставить признак успешной доставки.
     * Этот метод вызывается после получения обратного звонка от системы доставки,
     * подтверждающего успешную доставку заказа со склада
     *
     * @param deliveryId идентификатор доставки
     */
    @PostMapping("/api/v1/delivery/successful")
    DeliveryDto makeDeliverySuccess(@RequestBody(required = true) String deliveryId);

    /**
     * Эмуляция неудачного вручения товара.
     * Установить признак ошибки в доставке.
     * Этот метод вызывается, если поступает обратный звонок от службы доставки
     * и требуется изменить статус заказа в системе на «Неудачная доставка».
     *
     * @param deliveryId идентификатор доставки
     */
    @PostMapping("/api/v1/delivery/failed")
    DeliveryDto makeDeliveryFailed(@RequestBody(required = true) String deliveryId);

    /**
     * Расчёт полной стоимости доставки заказа.
     * Учитываются: адрес склада, адрес клиента, общий объём заказа, признак хрупкости и т.д.
     *
     * @param order заказ для расчёта.
     * @return полная стоимость доставки заказа.
     */
    @PostMapping("/api/v1/delivery/cost")
    Double calculateDeliveryCost(@RequestBody(required = true) OrderDto order);

}
