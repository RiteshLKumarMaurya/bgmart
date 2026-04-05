package com.biharigraphic.jilamart.payments.mappers;

import com.biharigraphic.jilamart.payments.model.dtos.PaymentRequest;
import com.biharigraphic.jilamart.payments.model.dtos.PaymentResponse;
import com.biharigraphic.jilamart.payments.model.dtos.PaymentResponseDTO;
import com.biharigraphic.jilamart.payments.model.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    //COD
    PaymentResponse toDto(Payment payment);

    //for the offline <-> COD
     Payment toEntity(PaymentRequest paymentDTO);



    //ONLINE
    PaymentResponseDTO toResponseDTO(Payment payment);
}