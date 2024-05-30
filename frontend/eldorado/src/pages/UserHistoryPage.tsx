import {useEffect, useState} from 'react';
import {Card, CardContent, CardTitle} from "@/components/ui/card.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {api} from "@/api/api.ts";
import {useTranslation} from "react-i18next";
import {localDateTimeToDate, UserHistoryType} from "@/types/Users.ts";
import {FiCheck, FiX} from "react-icons/fi";
import {Button} from "@/components/ui/button.tsx";

function UserHistoryPage({userId}: { userId?: string }) {
    const [currentPage, setCurrentPage] = useState(0);
    const [userHistory, setUserHistory] = useState<UserHistoryType[]>([]);
    const [pageSize] = useState(1);
    const {t} = useTranslation();

    const fetchUserHistory = () => {
        const endpoint = userId ? api.historyDataUser(userId, `?pageNumber=${currentPage}&pageSize=${pageSize}`) :
            api.historyDataSelf(`?pageNumber=${currentPage}&pageSize=${pageSize}`);
        endpoint.then(response => {
            if (response.status === 204) {
                if (currentPage > 0) setCurrentPage(currentPage - 1)
                setUserHistory([]);
            } else {
                setUserHistory(response.data);
            }
        }).catch(error => {
                console.log(error);
            }
        );
    };

    useEffect(() => {
        fetchUserHistory();
    }, [currentPage]);

    return (
        <div>
            {currentPage === 0 && userHistory.length === 0 ? (
                <Button variant={"ghost"} disabled={false}>{t("userHistory.noHistory")}</Button>
            ) : (
                <div>
                    {userHistory.map((history: UserHistoryType) => (
                        <Card key={history.id} className="mx-10 w-auto text-left">
                            <CardTitle className={"flex justify-center text-center mt-5"}>
                                {t("userHistory.version")}: {history.version}
                            </CardTitle>
                            <CardContent className={"mt-5"}>
                                <p><strong>{t("userHistory.id")}:</strong> {history.id}</p>
                                <p><strong>{t("userHistory.version")}:</strong> {history.version}</p>
                                <p><strong>{t("userHistory.login")}:</strong> {history.login}</p>
                                <p>
                                    <strong>{t("userHistory.suspended")}:</strong> {history.suspended ?
                                    <FiCheck color="red"/> : <FiX color="green"/>}
                                </p>
                                <p>
                                    <strong>{t("userHistory.active")}:</strong> {history.active ?
                                    <FiCheck color="green"/> : <FiX color="red"/>}
                                </p>
                                <p>
                                    <strong>{t("userHistory.blocked")}:</strong> {history.blocked ?
                                    <FiCheck color="red"/> : <FiX color="green"/>}
                                </p>
                                <p>
                                    <strong>{t("userHistory.twoFactorAuth")}:</strong> {history.twoFactorAuth ?
                                    <FiCheck color="green"/> : <FiX color="red"/>}
                                </p>
                                <p><strong>{t("userHistory.blockedTime")}:</strong> {history.blockedTime || 'N/A'}</p>
                                <p>
                                    <strong>{t("userHistory.lastSuccessfulLoginTime")}:</strong> {history.lastSuccessfulLoginTime ? localDateTimeToDate(history.lastSuccessfulLoginTime) : 'N/A'}
                                </p>
                                <p>
                                    <strong>{t("userHistory.lastUnsuccessfulLoginTime")}:</strong> {history.lastUnsuccessfulLoginTime ? localDateTimeToDate(history.lastUnsuccessfulLoginTime) : 'N/A'}
                                </p>
                                <p>
                                    <strong>{t("accountSettings.accountLanguage")}: </strong>
                                    {history?.accountLanguage?.toLowerCase() === 'en' ? t("general.english") :
                                        history?.accountLanguage?.toLowerCase() === 'pl' ? t("general.polish") :
                                            history?.accountLanguage}
                                </p>
                                <p>
                                    <strong>{t("userHistory.lastSuccessfulLoginIp")}:</strong> {history.lastSuccessfulLoginIp || 'N/A'}
                                </p>
                                <p>
                                    <strong>{t("userHistory.lastUnsuccessfulLoginIp")}:</strong> {history.lastUnsuccessfulLoginIp || 'N/A'}
                                </p>
                                <p><strong>{t("userHistory.phoneNumber")}:</strong> {history.phoneNumber}</p>
                                <p><strong>{t("userHistory.lastname")}:</strong> {history.lastname}</p>
                                <p><strong>{t("userHistory.name")}:</strong> {history.name}</p>
                                <p><strong>{t("userHistory.email")}:</strong> {history.email}</p>
                                <p><strong>{t("userHistory.operationType")}:</strong> {t(history.operationType)}</p>
                                <p>
                                    <strong>{t("userHistory.modificationTime")}:</strong> {localDateTimeToDate(history.modificationTime)}
                                </p>
                                <p><strong>{t("userHistory.modifiedBy")}:</strong> {history.modifiedBy || t("general.anonymous")}</p>
                            </CardContent>
                        </Card>
                    ))}
                </div>
            )}
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
                                    if (userHistory.length == 1) setCurrentPage(currentPage + 1)
                                }}
                            />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            </div>
        </div>
    );
}

export default UserHistoryPage;