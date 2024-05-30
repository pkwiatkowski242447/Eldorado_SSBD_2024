import { Card, CardContent, CardTitle } from "@/components/ui/card.tsx";
import { FiCheck, FiX } from "react-icons/fi";
import { useTranslation } from "react-i18next";

// @ts-expect-error idk
function DetailsForm({ account }) {
    const { t } = useTranslation();

    return (
        <div>
            <Card className="mx-10 w-auto text-left">
                <CardTitle className={"flex justify-center mt-5"}>
                    {t("accountSettings.accountInfo")}
                </CardTitle>
                <CardContent className={"mt-5"}>
                    <p>
                        <strong>{t("accountSettings.name")}:</strong> {account?.name} {account?.lastname}
                    </p>
                    <p><strong>{t("accountSettings.email")}:</strong> {account?.email}</p>
                    <p><strong>{t("accountSettings.login")}:</strong> {account?.login}</p>
                    <p><strong>{t("accountSettings.phone")}:</strong> {account?.phoneNumber}</p>
                    <p>
                        <strong>{t("accountSettings.accountLanguage")}: </strong>
                        {account?.accountLanguage?.toLowerCase() === 'en' ? t("general.english") :
                            account?.accountLanguage?.toLowerCase() === 'pl' ? t("general.polish") :
                                account?.accountLanguage}
                    </p>
                    <p>
                        <strong>{t("accountSettings.active")}:</strong> {account?.active ?
                        <FiCheck color="green"/> : <FiX color="red"/>}
                    </p>
                    <p>
                        <strong>{t("accountSettings.blocked")}:</strong> {account?.blocked ?
                        <FiCheck color="red"/> : <FiX color="green"/>}
                    </p>
                    <p>
                        <strong>{t("accountSettings.suspended")}:</strong> {account?.suspended ?
                        <FiCheck color="red"/> : <FiX color="green"/>}
                    </p>
                    <p>
                        <strong>{t("accountSettings.2fa")}:</strong> {account?.twoFactorAuth ?
                        <FiCheck color="green"/> : <FiX color="red"/>}
                    </p>
                    <p>
                        <strong>{t("accountSettings.creationDate")}:</strong> {account?.creationDate ? account.creationDate : 'N/A'}
                    </p>
                    <p>
                        <strong>{t("accountSettings.lastSucLoginTime")}:</strong> {account?.lastSuccessfulLoginTime ? account.lastSuccessfulLoginTime : 'N/A'}
                    </p>
                    <p>
                        <strong>{t("accountSettings.lastUnsucLoginTime")}:</strong> {account?.lastUnsuccessfulLoginTime ? account.lastUnsuccessfulLoginTime : 'N/A'}
                    </p>
                    <p>
                        <strong>{t("accountSettings.lastSucLoginIp")}:</strong> {account?.lastSuccessfulLoginIp || 'N/A'}
                    </p>
                    <p>
                        <strong>{t("accountSettings.lastUnsucLoginIp")}:</strong> {account?.lastUnsuccessfulLoginIp || 'N/A'}
                    </p>
                </CardContent>
            </Card>
        </div>
    );
}

export default DetailsForm;