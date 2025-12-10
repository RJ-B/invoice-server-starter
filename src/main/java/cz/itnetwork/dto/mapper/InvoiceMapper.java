package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface InvoiceMapper {

    Invoice toEntity(InvoiceDTO source);

    InvoiceDTO toDto(Invoice source);

    InvoiceReadDTO toReadDTO(Invoice source); // 🔥 Přidat tuto metodu
}
