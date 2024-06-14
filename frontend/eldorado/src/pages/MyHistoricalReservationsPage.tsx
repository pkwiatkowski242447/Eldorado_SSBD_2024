import {useEffect, useState} from "react";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {arrayToDate, ReservationType} from "@/types/Reservations.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {Spinner} from "react-bootstrap";
import {Button} from "@/components/ui/button.tsx";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Badge} from "@/components/ui/badge.tsx";


function MyHistoricalReservationsPage() {
    const [historicalReservations, setHistoricalReservations] = useState<ReservationType[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [pageSize] = useState(5);
    const navigate = useNavigate();
    const {t} = useTranslation();

    const fetchHistoricalReservations = async () => {
        setIsLoading(true);
        try {
            const response = await api.getHistoricalReservationsSelf(currentPage, pageSize);

            if (response.data) {
                const updatedReservations = response.data.map((reservation: ReservationType) => {
                    return {
                        ...reservation,
                        // @ts-expect-error ignore this for now
                        beginTime: reservation.beginTime ? arrayToDate(reservation.beginTime) : null,
                        // @ts-expect-error ignore this for now
                        endingTime: reservation.endingTime ? arrayToDate(reservation.endingTime) : null
                    };
                });
                setHistoricalReservations(updatedReservations);
            }
        } catch (error) {
            // @ts-expect-error ignore this for now
            handleApiError(error);
        } finally {
            setIsLoading(false);
        }
    }


    const handleViewDetails = (id: string) => {
        navigate(`/my-reservations/${id}`);
    };

    useEffect(() => {
        fetchHistoricalReservations();
    }, [currentPage, pageSize]);

    return (
        <div className="flex min-h-screen w-full flex-col">
            {isLoading ? (
                <Spinner/>
            ) : (
                <div className={"pt-5"}>
                    <Table className="p-10 flex-grow">
                        <TableHeader>
                            <TableRow className={"text-center p-10"}>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.begin.time")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.begin.time")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.city")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.street")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.zip.code")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.sector.name")}</TableHead>
                                <TableHead
                                    className="text-center">{t("my.historical.reservations.page.sector.id")}</TableHead>
                                <TableHead
                                    className="text-center"></TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody className={"text-center"}>
                            {historicalReservations.map(historicalReservation => (
                                <TableRow key={historicalReservation.id} className="flex-auto">
                                    <TableCell>{historicalReservation.beginTime}</TableCell>
                                    <TableCell>{historicalReservation.endingTime}</TableCell>
                                    <TableCell>{historicalReservation.city}</TableCell>
                                    <TableCell>{historicalReservation.street}</TableCell>
                                    <TableCell>{historicalReservation.zipCode}</TableCell>
                                    <TableCell>{historicalReservation.sectorName}</TableCell>
                                    <TableCell><Badge variant={"secondary"}>{t(historicalReservation.status)} </Badge></TableCell>
                                    <TableCell>{historicalReservation.id}</TableCell>
                                    <TableCell>
                                        <Button onClick={() => handleViewDetails(historicalReservation.id)}>
                                            {t("active.parking.info.page.view.info")}
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                    <div className={"pt-5"}>
                        <Pagination>
                            <PaginationContent>
                                <PaginationItem>
                                    <PaginationPrevious
                                        onClick={() => {
                                            if (currentPage > 0) setCurrentPage(currentPage - 1)
                                        }}
                                    />
                                </PaginationItem>
                                <PaginationItem>
                                    <PaginationLink>{currentPage + 1}</PaginationLink>
                                </PaginationItem>
                                <PaginationItem>
                                    <PaginationNext
                                        onClick={() => {
                                            if (historicalReservations.length === pageSize) setCurrentPage(currentPage + 1)
                                        }}
                                    />
                                </PaginationItem>
                            </PaginationContent>
                        </Pagination>
                    </div>
                </div>
            )}
        </div>
    )
}

export default MyHistoricalReservationsPage;