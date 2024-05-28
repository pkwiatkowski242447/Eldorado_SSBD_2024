import {Button} from "@/components/ui/button.tsx";
import {useAccount} from "@/hooks/useAccount.ts";
import {Loader2} from "lucide-react";
import {useState} from 'react';
import {useTranslation} from "react-i18next";

export function RefreshButton () {

    const {getCurrentAccount} = useAccount();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const {t} = useTranslation();

    async function refresh () {
        setIsRefreshing(true);
        await getCurrentAccount();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
            {isRefreshing ? (
                <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                </>
            ) : (
                t("general.refresh")
            )}
        </Button>
    )
}